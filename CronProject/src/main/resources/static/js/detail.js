function ActiveDetail(event){

    // 클릭 시 포스트 아이디의 Personal 값과 Id 값을 등록한 다음에
    // Get형식으로 보낸다.
    // fetch api를 통해서 전달을 하고 후에 이동을 한다?
    var postId = parseInt(event.currentTarget.getAttribute('data-post-id'), 10);
    var personal = event.currentTarget.getAttribute('data-post-personal');
    var check = event.currentTarget.getAttribute('data-post-check');


    if(check === 'true'){
        window.location.href = `/${personal}/post/${postId}`;
    }
    else{
        window.location.href = `/${personal}/comment/${postId}`;
    }

    event.stopPropagation();
}

