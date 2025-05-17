        // Nếu không có query string thì tự động set mẫu test
        const params = new URLSearchParams(window.location.search);
        // Existing appointment
        const title = params.get('title');
        const start = params.get('start');
        const end = params.get('end');
        const location = params.get('location');
        if(title) document.getElementById('conflict-title').textContent = title;
        if(start && end) {
            const s = new Date(start);
            const e = new Date(end);
            document.getElementById('conflict-time').textContent = `${s.toLocaleString()} - ${e.toLocaleTimeString()}`;
        }
        if(location) document.getElementById('conflict-location').textContent = location;
        // New appointment
        const ntitle = params.get('new_title');
        const nstart = params.get('new_start');
        const nend = params.get('new_end');
        const nlocation = params.get('new_location');
        if(ntitle) document.getElementById('new-title').textContent = ntitle;
        if(nstart && nend) {
            const s = new Date(nstart);
            const e = new Date(nend);
            document.getElementById('new-time').textContent = `${s.toLocaleString()} - ${e.toLocaleTimeString()}`;
        }
        if(nlocation) document.getElementById('new-location').textContent = nlocation;