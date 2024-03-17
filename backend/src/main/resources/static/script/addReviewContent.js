let dynamicFieldIndex = 0;

function addContent(element){
    var content = element.dataset.commentContent;
    var codeSelection = element.dataset.commentSelection;
    var dynamicFields = document.getElementById('dynamicFields');
    var divWrapper = document.createElement('div');
    dynamicFields.appendChild(divWrapper);
    var codeElement = document.createElement('code');
    divWrapper.appendChild(codeElement);
    var input = document.createElement('textarea');
    input.classList.add('review');
    input.required = true;
    input.name = 'content[' + dynamicFieldIndex + '].content';
    input.placeholder = 'Enter a comment';
    input.textContent = content
    divWrapper.appendChild(input);
    if (codeSelection !== undefined) {
        codeElement.innerText = codeSelection;
        var hiddenInput = document.createElement('input');
        hiddenInput.type = 'hidden';
        hiddenInput.name = 'content[' + (dynamicFieldIndex - 1) + '].codeSelection';
        hiddenInput.value = codeElement.innerText;
        divWrapper.appendChild(hiddenInput);
    }
    var deleteButton = document.createElement('button');
    deleteButton.textContent = 'Remove';
    deleteButton.classList.add('button', 'other');
    deleteButton.type = 'button';
    deleteButton.onclick = function() {
        if (dynamicFieldIndex > 1) {
            divWrapper.remove();
            dynamicFieldIndex--;
        } else {
            alert("You cannot remove the last field.");
        }
    };
    divWrapper.appendChild(deleteButton);
    dynamicFieldIndex++;
}
function addTextField() {
    var dynamicFields = document.getElementById('dynamicFields');

    var divWrapper = document.createElement('div');
    dynamicFields.appendChild(divWrapper);

    var codeElement = document.createElement('code');
    divWrapper.appendChild(codeElement);

    var input = document.createElement('textarea');
    input.classList.add('review');
    input.required = true;
    input.name = 'content[' + dynamicFieldIndex + '].content';
    input.placeholder = 'Enter a comment';
    divWrapper.appendChild(input);

    var copyButton = document.createElement('button');
    copyButton.classList.add('button', 'other');
    copyButton.textContent = 'Add selected code';
    copyButton.type = 'button';
    copyButton.onclick = function() {
        var selection = window.getSelection().toString().trim();
        var baseCode = document.getElementById('codeBlock').innerText;
        if (selection !== '') {
            codeElement.innerText = selection;
        }
        else {
            codeElement.innerText = baseCode;
        }
        var hiddenInput = document.createElement('input');
        hiddenInput.type = 'hidden';
        hiddenInput.name = 'content[' + (dynamicFieldIndex - 1) + '].codeSelection';
        hiddenInput.value = codeElement.innerText;
        divWrapper.appendChild(hiddenInput);
    };

    var deleteButton = document.createElement('button');
    deleteButton.textContent = 'Remove';
    deleteButton.classList.add('button', 'other');
    deleteButton.type = 'button';
    deleteButton.onclick = function() {
        if (dynamicFieldIndex > 1) {
            divWrapper.remove();
            dynamicFieldIndex--;
        } else {
            alert("You cannot remove the last field.");
        }
    };
    divWrapper.appendChild(copyButton);
    divWrapper.appendChild(deleteButton);
    dynamicFieldIndex++;
}