let current = new Date();

let appointments = []; // Khai báo ngoài, global hoặc trong scope chung
try {
    const el = document.getElementById("appointment-data");
    if (!el) throw new Error("Không tìm thấy phần tử #appointment-data");

    appointments = JSON.parse(el.dataset.appointments);

    // Convert startTime, endTime thành chuỗi ISO nếu chưa phải
    appointments = appointments.map(appt => ({
        ...appt,
        startTime: (typeof appt.startTime === 'string') ? appt.startTime : new Date(appt.startTime).toISOString(),
        endTime: (typeof appt.endTime === 'string') ? appt.endTime : new Date(appt.endTime).toISOString(),
    }));

    console.log("Appointments:", appointments);
} catch (err) {
    console.error("Lỗi parse JSON từ appointments:", err);
}

function renderCalendar() {
    const year = current.getFullYear();
    const month = current.getMonth();
    const monthNames = ["January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"];
    document.getElementById('calendar-title').textContent = monthNames[month] + ' ' + year;

    const firstDay = new Date(year, month, 1);
    const lastDay = new Date(year, month + 1, 0);
    const startDayOfWeek = firstDay.getDay();
    const totalDays = lastDay.getDate();

    const grid = document.getElementById('calendar-grid-month');
    while (grid.children.length > 1) grid.removeChild(grid.lastChild);

    let day = 1;
    const today = new Date();
    const isCurrentMonth = today.getFullYear() === year && today.getMonth() === month;

    for (let week = 0; week < 6 && day <= totalDays; week++) {
        const row = document.createElement('div');
        row.className = 'calendar-row calendar-cells';
        for (let d = 0; d < 7; d++) {
            const cell = document.createElement('div');
            cell.className = 'calendar-cell';

            if ((week === 0 && d < startDayOfWeek) || day > totalDays) {
                cell.innerHTML = '';
            } else {
                let cellContent = `<div class='cell-date`;
                if (isCurrentMonth && day === today.getDate()) {
                    cellContent += ` today-label`;
                    cell.classList.add('today-cell');
                }
                cellContent += `'>${day}</div>`;

                // Lọc các cuộc hẹn trong ngày
                const dayStr = `${year}-${(month + 1).toString().padStart(2, '0')}-${day.toString().padStart(2, '0')}`;
                const dailyAppointments = appointments.filter(appt => appt.startTime.startsWith(dayStr));

                dailyAppointments.forEach(apptData => {
                    const apptHtml = `<div class='appointment'
                        data-title='${apptData.name}'
                        data-location='${apptData.location}'
                        data-start='${apptData.startTime}'
                        data-end='${apptData.endTime}'
                        data-reminder='${apptData.reminder || "none"}'>
                        <b>${apptData.name}</b><br>${apptData.startTime.substring(11, 16)} - ${apptData.endTime.substring(11, 16)}<br>${apptData.location}
                    </div>`;
                    cellContent += apptHtml;
                });

                cell.innerHTML = cellContent;

                // Gán sự kiện click cho từng appointment
                cell.querySelectorAll('.appointment').forEach(appt => {
                    appt.onclick = function(e) {
                        e.stopPropagation();
                        showAppointmentModal({
                            title: appt.getAttribute('data-title'),
                            location: appt.getAttribute('data-location'),
                            start: appt.getAttribute('data-start'),
                            end: appt.getAttribute('data-end'),
                            reminder: appt.getAttribute('data-reminder')
                        });
                    };
                });

                day++;
            }

            row.appendChild(cell);
        }
        grid.appendChild(row);
    }
}

function showAppointmentModal(appt) {
    document.getElementById('modal-title').textContent = appt.title;
    document.getElementById('modal-content').innerHTML =
        `<b>Location:</b> ${appt.location}<br>` +
        `<b>Start:</b> ${formatDateTime(appt.start)}<br>` +
        `<b>End:</b> ${formatDateTime(appt.end)}<br>` +
        `<b>Reminder:</b> ${formatReminder(appt.reminder)}`;
    document.getElementById('appointment-modal').style.display = 'flex';
}

function formatDateTime(dt) {
    if (!dt) return '';
    const d = new Date(dt);
    return d.toLocaleString();
}

function formatReminder(val) {
    if (!val || val === 'none') return 'None';
    if (val === '5') return '5 minutes before';
    if (val === '10') return '10 minutes before';
    if (val === '30') return '30 minutes before';
    if (val === '60') return '1 hour before';
    return val;
}

// Đóng modal
document.getElementById('close-modal').onclick = function () {
    document.getElementById('appointment-modal').style.display = 'none';
};

document.getElementById('appointment-modal').onclick = function (e) {
    if (e.target === this) this.style.display = 'none';
};

// Điều hướng tháng
document.getElementById('prev-month').onclick = function () {
    current.setMonth(current.getMonth() - 1);
    renderCalendar();
};

document.getElementById('next-month').onclick = function () {
    current.setMonth(current.getMonth() + 1);
    renderCalendar();
};

// Gọi lần đầu khi trang load
renderCalendar();