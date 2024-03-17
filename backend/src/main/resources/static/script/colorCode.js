document.write('<script src="/prism/prism.js"></script>');

function colorCode(){
    const codeElement = document.querySelector('.language-java')
    codeElement.textContent = codeElement.getAttribute('data-code');;
}

function setupFileInput(fileInputId, codeElementId) {
    document.getElementById(fileInputId).addEventListener('change', function (e) {
        const file = e.target.files[0];
        if (file) {
            const reader = new FileReader();
            reader.onload = function (e) {
                const codeElement = document.getElementById(codeElementId);
                codeElement.textContent = e.target.result;
                Prism.highlightElement(codeElement);
                const preElement = codeElement.parentElement;
                if (e.target.result.trim().length > 0) {
                    preElement.style.display = 'block';
                } else {
                    preElement.style.display = 'none';
                }
            };
            reader.readAsText(file);
        }
    });
}