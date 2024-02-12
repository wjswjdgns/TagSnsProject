function redirectPost(elements){

    var postId = parseInt(elements.getAttribute('data-post-id'), 10);
    var postCheck = elements.getAttribute('data-post-check');

    let f = document.createElement('form');
    let obj;
    obj = document.createElement('input');
    obj.setAttribute('type', 'hidden');
    obj.setAttribute('name', 'postId');
    obj.setAttribute('value', postId);

    let obj2;
    obj2 = document.createElement('input');
    obj2.setAttribute('type', 'hidden');
    obj2.setAttribute('name', 'postCheck');
    obj2.setAttribute('value', postCheck);

    f.appendChild(obj);
    f.appendChild(obj2);
    f.setAttribute('method', 'post');
    f.setAttribute('action', '/compose/detail')
    document.body.appendChild(f);
    f.submit();
}