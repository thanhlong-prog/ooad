        // Giả lập: lấy thông tin nhóm từ query string hoặc backend
        // Ví dụ: ?title=Design%20Review&start=2025-05-23T09:00&end=2025-05-23T10:00&location=Conference%20Room&participants=Alice,Bob
        const params = new URLSearchParams(window.location.search);
        const title = params.get('title');
        const start = params.get('start');
        const end = params.get('end');
        const location = params.get('location');
        const participants = params.get('participants');
        if(title) document.getElementById('group-title').textContent = title;
        if(start && end) {
            const s = new Date(start);
            const e = new Date(end);
            document.getElementById('group-time').textContent = `${s.toLocaleString()} - ${e.toLocaleTimeString()}`;
        }
        if(location) document.getElementById('group-location').textContent = location;
        if(participants) document.getElementById('group-participants').textContent = 'Participants: ' + participants;