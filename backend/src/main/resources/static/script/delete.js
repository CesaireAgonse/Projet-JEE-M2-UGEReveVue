document.write('<script src="/script/notification.js"></script>');
function reload(){
    window.location.reload();
}

function back(){
    window.location.href = '/';
}

function backToCode(postId){
    window.location.href = '/codes/' + postId;
}

function backToReview(postId){
    window.location.href = '/reviews/' + postId;
}

function backToAdmin(){
    window.location.href = '/admin';
}

function del(url, dataAttribute, msg, update){
    document.querySelectorAll('.delete-btn').forEach(button => {
        button.addEventListener('click', function () {
            const form = this.closest('form');
            const data = form.getAttribute(dataAttribute);
            fetch(url + data, {
                method: 'DELETE'
            })
                .then(response => {
                    if (!response.ok) {
                        throw new Error('Failed to delete code');
                    }
                    const contentType = response.headers.get('content-type');
                    if (contentType && contentType.includes('application/json')) {
                        return response.json();
                    } else {
                        return null;
                    }
                })
                .then(() => {
                    showNotification(msg);
                    update()
                })
                .catch(error => console.error('Error:', error));
        });
    });
}

function delReview(){
    document.querySelectorAll('.delete-btn').forEach(button => {
        button.addEventListener('click', function() {
            const form = this.closest('form');
            const postId = form.getAttribute('data-post-id');
            const postType = form.getAttribute('data-post-type');
            if (postType === 'Code') {
                del('/api/v1/reviews/', 'data-code-id', 'Review deleted.', backToCode(postId))
            } else {
                del('/api/v1/reviews/', 'data-code-id', 'Review deleted.', backToReview(postId))
            }
        });
    });
}