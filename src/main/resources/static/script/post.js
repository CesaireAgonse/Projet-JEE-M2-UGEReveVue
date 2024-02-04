function votePost(voteButtonClass, postId, resultClass) {
    document.querySelectorAll(voteButtonClass).forEach(button => {
        button.addEventListener('click', function () {
            const form = this.closest('form');
            const codeId = form.getAttribute(postId);
            const voteType = this.value;
            fetch('../api/v1/posts/vote/' + codeId + '?voteType=' + voteType, {
                method: 'POST'
            })
                .then(response => response.json())
                .then(data => {
                    const scoreElement = form.querySelector(resultClass);
                    scoreElement.textContent = data;
                })
                .catch(error => console.error('Error:', error));
        });
    });
}

function getSelectedCode(codeBlock, codeSelection, preCodeSelection) {
    var selectedCode = "";
    var codeElement = document.getElementById(codeBlock);
    if (window.getSelection) {
        var selection = window.getSelection();
        if (selection.rangeCount > 0) {
            var range = selection.getRangeAt(0);
            if (codeElement.contains(range.startContainer) && codeElement.contains(range.endContainer)) {
                selectedCode = range.toString();
            }
        }
    } else if (document.selection && document.selection.type !== "Control") {
        var textRange = document.selection.createRange();
        if (codeElement.contains(textRange.parentElement())) {
            selectedCode = textRange.text;
        }
    }
    document.getElementById(codeSelection).value = selectedCode;
    document.getElementById(preCodeSelection).textContent = selectedCode;
}