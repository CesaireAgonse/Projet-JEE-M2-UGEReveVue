function showNotification(message, type = 'success') {
    if (!('Notification' in window)) {
        console.error('This browser does not support notifications.');
        return;
    }
    if (Notification.permission !== 'granted') {
        Notification.requestPermission().then(permission => {
            if (permission === 'granted') {
                displayNotification();
            }
        });
    } else {
        displayNotification();
    }
    function displayNotification() {
        new Notification('Notification', {
            body: message,
            icon: ''
        });
    }
}