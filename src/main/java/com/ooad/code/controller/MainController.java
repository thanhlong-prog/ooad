package com.ooad.code.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.ooad.code.model.Appointment;
import com.ooad.code.model.Reminder;
import com.ooad.code.model.User;
import com.ooad.code.service.AppointmentService;
import com.ooad.code.service.UserService;

@Controller
@RequestMapping("/user")
public class MainController {
    @Autowired
    private UserService userService;
    @Autowired
    private AppointmentService appointmentService;

    @RequestMapping("/list")
    public String list(Model model) throws JsonProcessingException {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        User consumer = null;
        if (principal instanceof UserDetails) {
            String username = ((UserDetails) principal).getUsername();
            consumer = userService.findByUsername(username);
        }

        if (consumer == null) {
            return "redirect:/login";
        }

        model.addAttribute("user", consumer);
        List<Appointment> appointments = appointmentService.getAllAppointmentsForUser(consumer);
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        String appointmentsJson = mapper.writeValueAsString(appointments);
        model.addAttribute("appointments", appointmentsJson);
        return "list";
    }

    @RequestMapping("/addpage")
    public String addPage(Model model) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        User user = null;
        if (principal instanceof UserDetails) {
            String username = ((UserDetails) principal).getUsername();
            user = userService.findByUsername(username);
        }

        if (user == null) {
            return "redirect:/login";
        }

        model.addAttribute("user", user);
        return "add";
    }

    @PostMapping("/add_appoint")
    public String add(@ModelAttribute Appointment appointment, @RequestParam("reminder") String reminderStr,
            Model model, RedirectAttributes redirectAttributes) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        User user = null;
        if (principal instanceof UserDetails) {
            String username = ((UserDetails) principal).getUsername();
            user = userService.findByUsername(username);
        }

        if (user == null) {
            return "redirect:/login";
        }

        appointment.setUser(user);

        if (!appointmentService.isValid(appointment)) {
            model.addAttribute("error", "Invalid appointment");
            model.addAttribute("user", user);
            return "add";
        }

        Appointment matchedGroup = appointmentService.findMatchingGroupMeeting(appointment);
        if (matchedGroup != null && matchedGroup.getUser() != user) {
            redirectAttributes.addFlashAttribute("groupMeeting", matchedGroup);
            return "redirect:/user/confirm_join";
        }

        if (appointmentService.hasConflict(user, appointment.getStartTime(), appointment.getEndTime())) {
            Appointment confictAppoint = appointmentService.findFirstAvailableAppointment(user,
                    appointment.getStartTime(), appointment.getEndTime());
            redirectAttributes.addFlashAttribute("conflictAppoint", confictAppoint);
            redirectAttributes.addFlashAttribute("appoint", appointment);
            return "redirect:/user/conflict";
        }

        if (!"none".equalsIgnoreCase(reminderStr)) {
            Reminder reminder = new Reminder();
            reminder.setType(reminderStr);
            reminder.setAppointment(appointment);
            appointment.getReminders().add(reminder);
        }

        appointmentService.saveAppointment(appointment);
        return "redirect:/user/list";
    }

    @RequestMapping("/confirm_join")
    public String confirmJoin(Model model,
            @ModelAttribute("groupMeeting") Appointment groupMeeting) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        User user = null;
        if (principal instanceof UserDetails) {
            String username = ((UserDetails) principal).getUsername();
            user = userService.findByUsername(username);
        }

        if (user == null) {
            return "redirect:/login";
        }
        model.addAttribute("user", user);
        model.addAttribute("groupMeeting", groupMeeting);
        List<User> participants = groupMeeting.getParticipants();
        model.addAttribute("participants", participants);
        return "confirm_join";
    }

    @GetMapping("/join_group")
    public String joinGroup(@RequestParam("groupId") Long appointmentId,
            RedirectAttributes redirectAttributes, Model model) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        User user = null;
        if (principal instanceof UserDetails) {
            String username = ((UserDetails) principal).getUsername();
            user = userService.findByUsername(username);
        }

        if (user == null) {
            return "redirect:/login";
        }
        model.addAttribute("user", user);
        if (appointmentService.joinGroup(appointmentId, user.getUsername()) == 1) {
            redirectAttributes.addFlashAttribute("success", "Successfully joined the group meeting!");
        } else {
            redirectAttributes.addFlashAttribute("error", "Failed to join the group meeting. Please try again.");
        }
        return "redirect:/user/list";
    }

    @RequestMapping("/conflict")
    public String handleConflict(Model model, @ModelAttribute("conflictAppoint") Appointment conflictAppoint,
            @ModelAttribute("appoint") Appointment appoint) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        User user = null;
        if (principal instanceof UserDetails) {
            String username = ((UserDetails) principal).getUsername();
            user = userService.findByUsername(username);
        }

        if (user == null) {
            return "redirect:/login";
        }
        model.addAttribute("user", user);
        model.addAttribute("conflictAppoint", conflictAppoint);
        model.addAttribute("appoint", appoint);
        return "conflict";
    }

    @GetMapping("/replace")
    public String replace(@RequestParam("conflictAppId") Long conflictAppId,
            @RequestParam("appName") String appName,
            @RequestParam("startTime") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime,
            @RequestParam("endTime") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime,
            @RequestParam("location") String location,
            RedirectAttributes redirectAttributes) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        User user = null;
        if (principal instanceof UserDetails) {
            String username = ((UserDetails) principal).getUsername();
            user = userService.findByUsername(username);
        }

        if (user == null) {
            return "redirect:/login";
        }
        Appointment app = new Appointment();
        app.setName(appName);
        app.setStartTime(startTime);
        app.setEndTime(endTime);
        app.setLocation(location);

        appointmentService.replace(conflictAppId, app, user);
        redirectAttributes.addFlashAttribute("success", "Successfully replaced the appointment!");
        return "redirect:/user/list";
    }

}
