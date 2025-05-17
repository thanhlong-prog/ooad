// Thêm biến lưu tháng/năm hiện tại
        let current = new Date();
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
                        // Demo: nếu là ngày 23 thì hiển thị 1 appointment mẫu
                        if (day === 23 && isCurrentMonth) {
                            cellContent += `<div class='appointment' data-title='Design Review' data-location='Conference Room' data-start='2025-05-23T09:00' data-end='2025-05-23T10:00' data-reminder='10'>
                                <b>Design Review</b><br>09:00 - 10:00<br>Conference Room</div>`;
                        }
                        cell.innerHTML = cellContent;
                        // Bắt sự kiện click vào appointment
                        const appt = cell.querySelector('.appointment');
                        if (appt) {
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
                        }
                        day++;
                    }
                    row.appendChild(cell);
                }
                grid.appendChild(row);
            }
        }
        renderCalendar();
        document.getElementById('prev-month').onclick = function() {
            current.setMonth(current.getMonth() - 1);
            renderCalendar();
        };
        document.getElementById('next-month').onclick = function() {
            current.setMonth(current.getMonth() + 1);
            renderCalendar();
        };
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
            if (val === 'none') return 'None';
            if (val === '5') return '5 minutes before';
            if (val === '10') return '10 minutes before';
            if (val === '30') return '30 minutes before';
            if (val === '60') return '1 hour before';
            return val;
        }
        document.getElementById('close-modal').onclick = function() {
            document.getElementById('appointment-modal').style.display = 'none';
        };
        // Đóng modal khi click ra ngoài
        document.getElementById('appointment-modal').onclick = function(e) {
            if (e.target === this) this.style.display = 'none';
        };