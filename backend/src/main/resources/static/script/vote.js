function votePost(auth) {
    if (auth !== '' && auth !== undefined && auth != null) {
        document.querySelectorAll('.vote-btn').forEach(button => {
            button.addEventListener('click', function () {
                const form = this.closest('form');
                const id = form.getAttribute('data-code-id');
                const voteType = this.value;
                fetch('../api/v1/posts/vote/' + id + '?voteType=' + voteType, {
                    method: 'POST'
                })
                    .then(response => response.json())
                    .then(data => {
                        const scoreElement = form.querySelector('.vote-score');
                        scoreElement.textContent = data;
                    })
                    .catch(error => console.error('Error:', error));
            });
        });
    }
}