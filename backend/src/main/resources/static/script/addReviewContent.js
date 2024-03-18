let dynamicFieldIndex = 0;

function addTextField() {
    const dynamicFields = document.getElementById('dynamicFields');
    const divWrapper = document.createElement('div');
    dynamicFields.appendChild(divWrapper);

    const codeElement = document.createElement('code');
    divWrapper.appendChild(codeElement);

    const input = this.getInput();
    divWrapper.appendChild(input);

    const copyButton = this.getCopyButton(codeElement, divWrapper);
    const deleteButton = this.getDeleteButton(divWrapper);
    divWrapper.appendChild(copyButton);
    divWrapper.appendChild(deleteButton);
    dynamicFieldIndex++;
}

function addReviewField(element) {
    const content = element.dataset.commentContent;
    const codeSelection = element.dataset.commentSelection;
    const dynamicFields = document.getElementById('dynamicFields');
    const divWrapper = document.createElement('div');
    dynamicFields.appendChild(divWrapper);

    const codeElement = document.createElement('code');
    divWrapper.appendChild(codeElement);

    const input = this.getInput();
    input.textContent = content
    divWrapper.appendChild(input);

    const copyButton = this.getCopyButton(codeElement, divWrapper, codeSelection);
    const deleteButton = this.getDeleteButton(divWrapper);
    divWrapper.appendChild(copyButton);
    divWrapper.appendChild(deleteButton);

    if (codeSelection !== undefined) {
        codeElement.innerText = codeSelection;
        var hiddenInput = document.createElement('input');
        hiddenInput.type = 'hidden';
        hiddenInput.name = 'content[' + (dynamicFieldIndex - 1) + '].codeSelection';
        hiddenInput.value = codeElement.innerText;
        divWrapper.appendChild(hiddenInput);
        const existingDeleteButton = divWrapper.querySelector('.delete-code-button');
        if (!existingDeleteButton) {
            var deleteCodeButton = getDeleteCodeButton(codeElement, hiddenInput)
            divWrapper.appendChild(deleteCodeButton);
        }
    }
    dynamicFieldIndex++;
}

function getCopyButton(codeElement, divWrapper) {
    var copyButton = document.createElement('button');
    copyButton.classList.add('button', 'other');
    copyButton.textContent = 'Add selected code';
    copyButton.type = 'button';
    copyButton.onclick = function () {
        var selection = window.getSelection().toString().trim();
        if (selection === ''){
            alert("Please select a piece of code by highlighting it with your mouse.")
            return
        }
        codeElement.innerText = selection;
        var existingHiddenInput = divWrapper.querySelector('input[type=hidden]');
        if (existingHiddenInput) {
            existingHiddenInput.remove();
        }
        var hiddenInput = document.createElement('input');
        hiddenInput.type = 'hidden';
        hiddenInput.name = 'content[' + (dynamicFieldIndex - 1) + '].codeSelection';
        hiddenInput.value = codeElement.innerText;
        divWrapper.appendChild(hiddenInput);

        var existingDeleteButton = divWrapper.querySelector('.delete-code-button');
        if (!existingDeleteButton) {
            var deleteCodeButton = getDeleteCodeButton(codeElement, hiddenInput)
            divWrapper.appendChild(deleteCodeButton);
        }
    };
    return copyButton
}

function getDeleteButton(divWrapper){
    var deleteButton = document.createElement('button');
    deleteButton.textContent = 'Remove field';
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
    return deleteButton;
}

function getInput(){
    const input = document.createElement('textarea');
    input.classList.add('review');
    input.required = true;
    input.name = 'content[' + dynamicFieldIndex + '].content';
    input.placeholder = 'Enter a comment';
    return input;
}

function getDeleteCodeButton(codeElement, hiddenInput){
    const deleteCodeButton = document.createElement('button');
    deleteCodeButton.textContent = 'Delete selected code';
    deleteCodeButton.classList.add('button', 'other', 'delete-code-button');
    deleteCodeButton.type = 'button';
    deleteCodeButton.onclick = function () {
        codeElement.innerText = '';
        hiddenInput.value = '';
    };
    return deleteCodeButton;
}