// 저장하기
function SendNotificationData(){


    // personal 값을 받아오기
    var personal = document.querySelector(".personalMember").getAttribute('data-personal');

    // 사용자 알림의 값을 가져오기
    var NoPost = document.querySelector("#NoPost").checked;
    var allPost = document.querySelector("#allPost").checked;
    var allPostComment = document.querySelector("#allPostComment").checked;

    let checkTagList = [];
    let removeList = [];

    // 태그 알림 값들 가져오기
    let tagItems = document.querySelectorAll(".setting-area");
    tagItems.forEach((item) => {
        if(item.querySelector(".tag-checked").checked){
            checkTagList.push(parseInt(item.getAttribute('data-tag-id'),10));
        }
        else{
            removeList.push(parseInt(item.getAttribute('data-tag-id'),10));
        }
    });


    var status;

    // 모든 포스터
    if(allPost == true){
        status = 1;
    }
    // 모든 포스터 및 댓글
    else if(allPostComment == true){
        status = 2;
    }
    else{
        status = 0;
    }



    fetch(`/${personal}/notifications`, {
       method : 'POST',
       headers : {
           'Content-Type' : 'application/json'
       },
       body : JSON.stringify({
           status : status,
           tagList : checkTagList,
           removeList : removeList
       })
    })
    .then(response => {
        goBack();
    })
    .catch(error => {
       console.error("오류 발생 : ", error);
    });


}