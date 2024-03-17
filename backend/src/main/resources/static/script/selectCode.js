function getSelectedCode() {
    let selectedCode = "";
    const codeElement = document.getElementById("codeBlock");
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
    document.getElementById("codeSelection").value = selectedCode;
    document.getElementById("preCodeSelection").textContent = selectedCode;
}