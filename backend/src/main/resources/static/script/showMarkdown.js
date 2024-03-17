document.write('<script src="/markdown-it/markdown-it.min.js"></script>');

function markdown(){
    const md = window.markdownit();
    const divs = document.getElementsByClassName('markdown');
    for (let i = 0; i < divs.length; i++) {
        if (!divs[i].hasAttribute('data-rendered')) {
            var content = divs[i].innerHTML;
            divs[i].innerHTML = md.render(content);
            divs[i].setAttribute('data-rendered', 'true');
        }
    }
}