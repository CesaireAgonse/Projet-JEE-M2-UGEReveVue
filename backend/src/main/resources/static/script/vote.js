function votePost(auth) {
    if (auth !== '' && auth !== undefined && auth != null) {
        document.querySelectorAll('.vote-btn').forEach(button => {
            button.addEventListener('click', function () {
                const form = this.closest('form');
                const id = form.getAttribute('data-code-id');
                const voteType = this.value;
                const isSelected = this.classList.contains('btn-selected');
                fetch('../api/v1/posts/vote/' + id + '?voteType=' + voteType, {
                    method: 'POST'
                })
                    .then(response => response.json())
                    .then(data => {
                        const scoreElement = form.querySelector('.vote-score');
                        scoreElement.textContent = data;
                        form.querySelectorAll('.vote-btn').forEach(btn => {
                            if (btn.value === voteType) {
                                if (btn.classList.contains('btn-selected')){
                                    btn.classList.remove('btn-selected');
                                    btn.classList.add('btn');
                                }else {
                                    btn.classList.add('btn-selected');
                                    btn.classList.remove('btn');
                                }
                            } else {
                                btn.classList.remove('btn-selected');
                                btn.classList.add('btn');
                            }
                        })
                    })
                    .catch(error => console.error('Error:', error));
            });
        });
    }
}