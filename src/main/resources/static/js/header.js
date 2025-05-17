document.addEventListener('DOMContentLoaded', () => {
    // Lấy các phần tử
    const menuToggle = document.querySelector('.menu');
    const notificationToggle = document.querySelector('.notification');
    const profileToggle = document.querySelector('.profile');
    const menuDropdown = document.querySelector('.menu-dropdown');
    const notificationDropdown = document.querySelector('.notification-dropdown');
    const profileDropdown = document.querySelector('.profile-dropdown');

    // Hàm đóng tất cả dropdown
    const closeAllDropdowns = () => {
        menuDropdown.classList.remove('active');
        notificationDropdown.classList.remove('active');
        profileDropdown.classList.remove('active');
    };

    // Sự kiện nhấp chuột cho menu
    menuToggle.addEventListener('click', (e) => {
        e.stopPropagation();
        const isActive = menuDropdown.classList.contains('active');
        closeAllDropdowns();
        if (!isActive) {
            menuDropdown.classList.add('active');
        }
    });

    // Sự kiện nhấp chuột cho notification
    notificationToggle.addEventListener('click', (e) => {
        e.stopPropagation();
        const isActive = notificationDropdown.classList.contains('active');
        closeAllDropdowns();
        if (!isActive) {
            notificationDropdown.classList.add('active');
        }
    });

    // Sự kiện nhấp chuột cho profile
    profileToggle.addEventListener('click', (e) => {
        e.stopPropagation();
        const isActive = profileDropdown.classList.contains('active');
        closeAllDropdowns();
        if (!isActive) {
            profileDropdown.classList.add('active');
        }
    });

    // Đóng dropdown khi nhấp ra ngoài
    document.addEventListener('click', (e) => {
        if (!menuToggle.contains(e.target) && !menuDropdown.contains(e.target) &&
            !notificationToggle.contains(e.target) && !notificationDropdown.contains(e.target) &&
            !profileToggle.contains(e.target) && !profileDropdown.contains(e.target)) {
            closeAllDropdowns();
        }
    });
});