
// 리트윗 활성화 이미지 변경
function RetwitClick(element){
    var currentSrc = element.src; // 리트윗 아이콘의 (src) 정보를 가져온다.
    // 이미지의 현재 소스와 대상 소스를 비교하여 새로운 소스를 설정합니다.
    var newSrc = currentSrc.endsWith('retwit.png') ? '/img/rfill.png' : '/img/retwit.png';
    // 소스를 새로 설정
    element.src = newSrc;
}

// 리트윗 값 계산

function RetwitCount(countElement, retwitCount, increase){
    if(increase){
        retwitCount++;
    }
    else{
        retwitCount--;
    }

    countElement.textContent = retwitCount;
}

// 상세페이지 리트윗 기능 추가
function sendRetwitDataDetail(){
    var postId = parseInt(document.querySelector('.detail-post').getAttribute('data-post-id'), 10); // 포스트
    var postCheck = document.querySelector('.detail-post').getAttribute('data-post-check'); // 포스트 체크
    var retwitCheck = document.querySelector('.detail-icon').querySelector('.retwit-icon').getAttribute('data-post-retwit'); // 현재 리트윗 체크

    // 좋아요 수치 확인
    var countElement = document.querySelector('.detail-post-info').querySelector('.detail-retwit-count');
    var retwitCount = parseInt(countElement.textContent);

    if(retwitCheck === "false"){
            // 리트윗 남기기
             fetch('/retwit/add', {
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

                    // 리트윗 변경하기
                    var retwitInfo = document.querySelector('.detail-icon').querySelector('.retwit-icon');
                    retwitInfo.setAttribute('data-post-retwit',true);
                    RetwitClick(retwitInfo);


                    // 리트윗 값 줄이기
                    RetwitCount(countElement,retwitCount,true);

             })
             .catch(error => {
                console.error("오류 발생 : ", error);
             });
         }
         else{
            // 리트윗 취소하기
            fetch('/retwit/cancel', {
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

                    // 리트윗 변경하기
                    var retwitInfo = document.querySelector('.detail-icon').querySelector('.retwit-icon');
                    retwitInfo.setAttribute('data-post-retwit',false);
                    RetwitClick(retwitInfo);

                    // 리트윗 값 줄이기
                    RetwitCount(countElement,retwitCount,false);
                 })
                 .catch(error => {
                    console.error("오류 발생 : ", error);
                 });
            }

}

// 상세페이지 댓글 리트윗
function sendRetwitDataDetailComment(event){

    // 리트윗 선택
    var retwitInfo = event.currentTarget.querySelector('.retwit-icon');

    var postId = parseInt(event.currentTarget.closest('.commnet-user-area').getAttribute('data-post-id'), 10);
    var postCheck = event.currentTarget.closest('.commnet-user-area').getAttribute('data-post-check');

    var retwitCheck = event.currentTarget.querySelector('.retwit-icon').getAttribute('data-post-retwit'); // 현재 리트윗 체크

    // 좋아요 수치 확인
    var countElement = event.currentTarget.querySelector('.retwit-count');
    var retwitCount = parseInt(countElement.textContent);

    if(retwitCheck === "false"){
            // 리트윗 남기기
             fetch('/retwit/add', {
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
                    retwitInfo.setAttribute('data-post-retwit',true);
                    RetwitClick(retwitInfo);

                    // 리트윗 값 줄이기
                    RetwitCount(countElement,retwitCount,true);

             })
             .catch(error => {
                console.error("오류 발생 : ", error);
             });
         }
         else{
            // 리트윗 취소하기
            fetch('/retwit/cancel', {
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

                    retwitInfo.setAttribute('data-post-retwit',false);
                    RetwitClick(retwitInfo);

                    // 리트윗 값 줄이기
                    RetwitCount(countElement,retwitCount,false);
                 })
                 .catch(error => {
                    console.error("오류 발생 : ", error);
                 });
            }
    event.stopPropagation();
}

// 리트윗 데이터 전달
function sendRetwitData(event){

     var postId = parseInt(event.currentTarget.closest('.postarea').getAttribute('data-post-id'), 10); //포스트
     var postCheck = event.currentTarget.closest('.postarea').getAttribute('data-post-check'); //포스트체크
     var retwitCheck = event.currentTarget.querySelector('.retwit-icon').getAttribute('data-post-retwit');

     var countElement = event.currentTarget.querySelector('.retwit-count');
     var retwitCount = parseInt(countElement.textContent);



     if(retwitCheck === "false"){
        // 리트윗 남기기
         fetch('/retwit/add', {
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
            const target = document.querySelectorAll(`.postarea[data-post-id="${postId}"][data-post-check="${postCheck}"] img.retwit-icon`);
            const target_count = document.querySelectorAll(`.postarea[data-post-id="${postId}"][data-post-check="${postCheck}"] p.retwit-count`);

            const elementsArray = Array.from(target);
            const elementsArray2 = Array.from(target_count);

           elementsArray.forEach(element => {
                element.setAttribute('data-post-retwit', true);
                RetwitClick(element);
           });

           elementsArray2.forEach(element => {
                RetwitCount(element, retwitCount, true);
           });


            // 값 변경 진행
         })
         .catch(error => {
            console.error("오류 발생 : ", error);
         });
     }
     else{
        // 리트윗 취소하기
        fetch('/retwit/cancel', {
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
               const target = document.querySelectorAll(`.postarea[data-post-id="${postId}"][data-post-check="${postCheck}"] img.retwit-icon`);
               const target_count = document.querySelectorAll(`.postarea[data-post-id="${postId}"][data-post-check="${postCheck}"] p.retwit-count`);

               const elementsArray = Array.from(target);
               const elementsArray2 = Array.from(target_count);

              elementsArray.forEach(element => {
                   element.setAttribute('data-post-retwit', false);
                   RetwitClick(element);
              });

              elementsArray2.forEach(element => {
                   RetwitCount(element, retwitCount, false);
              });


             })
             .catch(error => {
                console.error("오류 발생 : ", error);
             });
        }

    event.stopPropagation();

}
