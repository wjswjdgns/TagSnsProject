/**
* 디폴트 더보기
*/

// 더보기 보여주기
function showSelectOptions(event) {

    var postId = parseInt(event.currentTarget.closest('.postarea').getAttribute('data-post-id'), 10); //포스트
    var postCheck = event.currentTarget.closest('.postarea').getAttribute('data-post-check'); // 포스트 체크

    // 옵션 넣어주기
    document.querySelector('.dim-select-area').setAttribute('data-post-id', postId);
    document.querySelector('.dim-select-area').setAttribute('data-post-check', postCheck);

    document.querySelector('.dim').style.display = 'block';
    document.querySelector('.dim-select-area').style.display = 'block';
    event.stopPropagation();
}
// 취소하기
function exitSelectOptions(event){
    document.querySelector('.dim').style.display = 'none';
    document.querySelector('.dim-select-area').style.display = 'none';
    document.querySelector('.dim-setting-area').style.display = 'none';
}

// 글 수정
function PostEditEvent(element){

    var postId = parseInt(element.closest('.dim-select-area').getAttribute('data-post-id'), 10);
    var postCheck = element.closest('.dim-select-area').getAttribute('data-post-check');

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
     f.setAttribute('action', '/compose/edit')
     document.body.appendChild(f);
     f.submit();

}

// 글 삭제
function PostDeleteEvent(element){
    // ID 값을 가져와서 삭제를 진행할 수 있도록 한다.

    var postId = parseInt(element.closest('.dim-select-area').getAttribute('data-post-id'), 10);
    var postCheck = element.closest('.dim-select-area').getAttribute('data-post-check');

     // 포스트 삭제하기
     fetch('/write/delete', {
        method : 'POST',
        headers : {
            'Content-Type' : 'application/json'
        },
        body : JSON.stringify({
            postId : postId,
            postCheck : postCheck
        })
     })
     .then(response => {
        // 삭제가 완료 되었다면 딤을 닫고
        document.querySelector('.dim').style.display = 'none';
        document.querySelector('.dim-select-area').style.display = 'none';

        // 현재 페이지 새로고침
        window.location.reload();
     })
     .catch(error => {
        console.error("오류 발생 : ", error);
     });

}


/**
* 상세 페이지 더보기
*/

// 상세 글
function DetailMainShowSelectOptions(event){
    var postId = parseInt(event.currentTarget.closest('.detail-post').getAttribute('data-post-id'), 10); //포스트
    var postCheck = event.currentTarget.closest('.detail-post').getAttribute('data-post-check'); // 포스트 체크

    // 옵션 넣어주기
    document.querySelector('.dim-select-area').setAttribute('data-post-id', postId);
    document.querySelector('.dim-select-area').setAttribute('data-post-check', postCheck);

    document.querySelector('.dim').style.display = 'block';
    document.querySelector('.dim-select-area').style.display = 'block';
    event.stopPropagation();
}

// 댓글
function DetailSubShowSelectOptions(event) {

    var postId = parseInt(event.currentTarget.closest('.commnet-user-area').getAttribute('data-post-id'), 10); //포스트
    var postCheck = event.currentTarget.closest('.commnet-user-area').getAttribute('data-post-check'); // 포스트 체크

    // 옵션 넣어주기
    document.querySelector('.dim-select-area').setAttribute('data-post-id', postId);
    document.querySelector('.dim-select-area').setAttribute('data-post-check', postCheck);

    document.querySelector('.dim').style.display = 'block';
    document.querySelector('.dim-select-area').style.display = 'block';
    event.stopPropagation();
}

// 설정
function setting(){
    document.querySelector('.dim').style.display = 'block';
    document.querySelector('.dim-setting-area').style.display = 'block';
}