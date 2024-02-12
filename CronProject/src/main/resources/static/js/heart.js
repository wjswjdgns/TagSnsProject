 function heartClick(event){
     // 클릭된 이미지의 현재 소스(src)를 가져옵니다.
     var currentSrc = event.src;
     // 이미지의 현재 소스와 대상 소스를 비교하여 새로운 소스를 설정합니다.
     var newSrc = currentSrc.endsWith('heart.png') ? '/img/fill.png' : '/img/heart.png';
     // 이미지의 소스를 새로 설정합니다.
     event.src = newSrc;
 }



 // 좋아요 count 계산
 function updateLikesCount(countElement,likesCount,increase){
     if(increase){
         likesCount++;
     }
     else{
         likesCount--;
     }
     countElement.textContent = likesCount;
 }


// 상세페이지 좋아요 기능 추가
function sendHeartDataDetail(){

    // 포스트
    // 포스트 체크
    // 포스트 하트 체크

    var postId = parseInt(document.querySelector('.detail-post').getAttribute('data-post-id'), 10); // 포스트
    var postCheck = document.querySelector('.detail-post').getAttribute('data-post-check'); // 포스트 체크
    var likeCheck = document.querySelector('.detail-icon').querySelector('.heart-icon').getAttribute('data-post-heart'); // 현재 하트 체크

    // 좋아요 수치 확인
    var countElement = document.querySelector('.detail-post-info').querySelector('.detail-heart-count');
    var likesCount = parseInt(countElement.textContent);


    if(likeCheck == 'false'){

             // 좋아요 남기기
             fetch('/like/add', {
                method : 'POST',
                headers : {
                    'Content-Type' : 'application/json'
                },
                body : JSON.stringify({
                    checkId : postId,
                    checkPost : postCheck
                })
             })
             .then(response => {

                // 하트 변경하기
                var heartInfo = document.querySelector('.detail-icon').querySelector('.heart-icon');
                heartInfo.setAttribute('data-post-heart',true);
                heartClick(heartInfo);

                // 하트 값 늘리기
                updateLikesCount(countElement,likesCount,true);

             })
             .catch(error => {
                console.error("오류 발생 : ", error);
             });
        }

        else if(likeCheck == 'true'){
            // 좋아요 취소하기
             fetch('/like/cancel', {
                method : 'POST',
                headers : {
                    'Content-Type' : 'application/json'
                },
                body : JSON.stringify({
                    checkId : postId,
                    checkPost : postCheck
                })
             })
             .then(response => {

                 // 하트 변경하기
                 var heartInfo = document.querySelector('.detail-icon').querySelector('.heart-icon');
                 heartInfo.setAttribute('data-post-heart',false);
                 heartClick(heartInfo);

                 // 하트 값 늘리기
                 updateLikesCount(countElement,likesCount,false);

             })
             .catch(error => {
                console.error("오류 발생 : ", error);
             });
        }
        else{
            alert('데이터 저장에 오류가 발생했습니다.');
        }

}

// 상세페이지 댓글 좋아요 기능 추가
function sendHeartDataDetailComment(event){

    // 하트 하트 아이콘 변수 등록
    var heartInfo = event.currentTarget.querySelector('.heart-icon');

    // 데이터를 저장하기
    var postId = parseInt(event.currentTarget.closest('.commnet-user-area').getAttribute('data-post-id'), 10);
    var postCheck = event.currentTarget.closest('.commnet-user-area').getAttribute('data-post-check');

    var likeCheck = event.currentTarget.querySelector('.heart-icon').getAttribute('data-post-heart'); // 현재 하트 체크

    // 좋아요 수치 확인
    var countElement = event.currentTarget.querySelector('.heart-count');
    var likesCount = parseInt(countElement.textContent);



    if(likeCheck == 'false'){
                 // 좋아요 남기기
                 fetch('/like/add', {
                    method : 'POST',
                    headers : {
                        'Content-Type' : 'application/json'
                    },
                    body : JSON.stringify({
                        checkId : postId,
                        checkPost : postCheck
                    })
                 })
                 .then(response => {


                    heartInfo.setAttribute('data-post-heart',true);
                    heartClick(heartInfo);

                    // 하트 값 늘리기
                    updateLikesCount(countElement,likesCount,true);

                 })
                 .catch(error => {
                    console.error("오류 발생 : ", error);
                 });
            }

            else if(likeCheck == 'true'){
                // 좋아요 취소하기
                 fetch('/like/cancel', {
                    method : 'POST',
                    headers : {
                        'Content-Type' : 'application/json'
                    },
                    body : JSON.stringify({
                        checkId : postId,
                        checkPost : postCheck
                    })
                 })
                 .then(response => {

                     heartInfo.setAttribute('data-post-heart',false);
                     heartClick(heartInfo);

                     // 하트 값 늘리기
                     updateLikesCount(countElement,likesCount,false);

                 })
                 .catch(error => {
                    console.error("오류 발생 : ", error);
                 });
            }
            else{
                alert('데이터 저장에 오류가 발생했습니다.');
            }

    event.stopPropagation();
}



// Post에 하트를 선택하여 늘리는 데이터 전달
function sendHeartData(event){

    // 데이터를 저장하기
    var postId = parseInt(event.currentTarget.closest('.postarea').getAttribute('data-post-id'), 10); //포스트
    var postCheck = event.currentTarget.closest('.postarea').getAttribute('data-post-check'); // 포스트 체크
    var likeCheck = event.currentTarget.querySelector('.heart-icon').getAttribute('data-post-heart'); // 현재 하트 체크


    // 좋아요 숫자 확인
    var countElement = event.currentTarget.querySelector('.heart-count');
    var likesCount = parseInt(countElement.textContent);



    if(likeCheck == 'false'){

         // 좋아요 남기기
         fetch('/like/add', {
            method : 'POST',
            headers : {
                'Content-Type' : 'application/json'
            },
            body : JSON.stringify({
                checkId : postId,
                checkPost : postCheck
            })
         })
         .then(response => {
            const target = document.querySelectorAll(`.postarea[data-post-id="${postId}"][data-post-check="${postCheck}"] img.heart-icon`);
            const target_count = document.querySelectorAll(`.postarea[data-post-id="${postId}"][data-post-check="${postCheck}"] p.heart-count`);


            const elementsArray = Array.from(target);
            const elementsArray2 = Array.from(target_count);


            elementsArray.forEach(element => {
                element.setAttribute('data-post-heart', true);
                heartClick(element);
            });

            elementsArray2.forEach(element => {
                updateLikesCount(element, likesCount, true);
            });


         })
         .catch(error => {
            console.error("오류 발생 : ", error);
         });
    }

    else if(likeCheck == 'true'){
        // 좋아요 취소하기
         fetch('/like/cancel', {
            method : 'POST',
            headers : {
                'Content-Type' : 'application/json'
            },
            body : JSON.stringify({
                checkId : postId,
                checkPost : postCheck
            })
         })
         .then(response => {

            const target = document.querySelectorAll(`.postarea[data-post-id="${postId}"][data-post-check="${postCheck}"] img.heart-icon`);
            const target_count = document.querySelectorAll(`.postarea[data-post-id="${postId}"][data-post-check="${postCheck}"] p.heart-count`);

            const elementsArray = Array.from(target);
            const elementsArray2 = Array.from(target_count);

            elementsArray.forEach(element => {
                element.setAttribute('data-post-heart', false);
                heartClick(element);
            });

            elementsArray2.forEach(element => {
                updateLikesCount(element, likesCount, false);
            });

         })
         .catch(error => {
            console.error("오류 발생 : ", error);
         });
    }
    else{
        alert('데이터 저장에 오류가 발생했습니다.');
    }


    event.stopPropagation();
}

