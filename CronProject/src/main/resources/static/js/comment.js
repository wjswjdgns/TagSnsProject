
// Post에 하트를 선택하여 늘리는 데이터 전달

function writeComment(event){

    // get 형태로 /compose/comment 이곳으로 이동
    // 데이터를 전달을 한다.

     let f = document.createElement('form');
     var postId = parseInt(event.currentTarget.closest('.postarea').getAttribute('data-post-id'), 10); //포스트
     var postCheck = event.currentTarget.closest('.postarea').getAttribute('data-post-check'); //포스트체크

     let obj;
     obj = document.createElement('input');
     obj.setAttribute('type', 'hidden');
     obj.setAttribute('name', 'postId');
     obj.setAttribute('value', postId);

     let obj2;
     obj2 = document.createElement('input');
     obj2.setAttribute('type', 'hidden');
     obj2.setAttribute('name', 'check');
     obj2.setAttribute('value', postCheck);

     f.appendChild(obj);
     f.appendChild(obj2);
     f.setAttribute('method', 'post');
     f.setAttribute('action', '/compose/comment')
     document.body.appendChild(f);
     f.submit();

    event.stopPropagation();
}

function detailwriteComment(){
    let f = document.createElement('form');
    var postId = parseInt(document.querySelector('.detail-post').getAttribute('data-post-id'), 10); // 포스트
    var postCheck = document.querySelector('.detail-post').getAttribute('data-post-check'); // 포스트 체크

     let obj;
         obj = document.createElement('input');
         obj.setAttribute('type', 'hidden');
         obj.setAttribute('name', 'postId');
         obj.setAttribute('value', postId);

         let obj2;
         obj2 = document.createElement('input');
         obj2.setAttribute('type', 'hidden');
         obj2.setAttribute('name', 'check');
         obj2.setAttribute('value', postCheck);

         f.appendChild(obj);
         f.appendChild(obj2);
         f.setAttribute('method', 'post');
         f.setAttribute('action', '/compose/comment')
         document.body.appendChild(f);
         f.submit();
}

function detailwriteCommentComment(event){

    let f = document.createElement('form');
    var postId = parseInt(event.currentTarget.closest('.commnet-user-area').getAttribute('data-post-id'), 10);
    var postCheck = event.currentTarget.closest('.commnet-user-area').getAttribute('data-post-check');

     let obj;
         obj = document.createElement('input');
         obj.setAttribute('type', 'hidden');
         obj.setAttribute('name', 'postId');
         obj.setAttribute('value', postId);

         let obj2;
         obj2 = document.createElement('input');
         obj2.setAttribute('type', 'hidden');
         obj2.setAttribute('name', 'check');
         obj2.setAttribute('value', postCheck);

         f.appendChild(obj);
         f.appendChild(obj2);
         f.setAttribute('method', 'post');
         f.setAttribute('action', '/compose/comment')
         document.body.appendChild(f);
         f.submit();


    event.stopPropagation();

}