// 페이지 로드 시 현재 페이지 저장
window.addEventListener('load', storePageInfo);

// 현재 페이지를 저장
function storePageInfo(){

    // 현재 페이지 URL 저장
    const pageInfo ={
        url : window.location.href,
    };

   // 세션 스토리지에 저장된 페이지 정보를 가져옴 (이전에 저장된 정보들)
   let storedPages = JSON.parse(sessionStorage.getItem('pageStack')) || [];

   // 이전에 동일한 url이 있다면 스택 추가를 멈춘다.
   const isUrlAlreadyStored = storedPages.some(page => page.url === pageInfo.url);
   if(!isUrlAlreadyStored){
       // 현재 페이지 정보를 스택에 추가
       storedPages.push(pageInfo);

       // 세션 스토리지에 업데이트 된 스택을 저장
       sessionStorage.setItem('pageStack', JSON.stringify(storedPages));
   }
}

// 뒤로가기 클릭 시 페이지 가져오기
function goBack() {
    let storedPages = JSON.parse(sessionStorage.getItem('pageStack')) || [];

    // 이전 페이지가 있는 경우
    if(storedPages.length > 1){
        //현재 페이지 정보를 스택에서 제거
        storedPages.pop();

        // 이전 페이지 정보를 가져옴 (제거 후 마지막 정보)
        const previousPage = storedPages[storedPages.length - 1];

        // 세션 스토리지 업데이트
        sessionStorage.setItem('pageStack', JSON.stringify(storedPages));

        // 이전 페이지로 이동
        window.location.href = previousPage.url;
    }
    // 이전 페이지가 없는 경우 기본적으로 뒤로가기 진행
    else{
        window.history.back();
    }
}

function CleanValue(){
    sessionStorage.clear();
}


// 탭으로 이동 했을 경우 세션 처리
function TabMove(arg){
    let storedPages = JSON.parse(sessionStorage.getItem('pageStack')) || [];

    //현재 페이지 정보를 스택에서 제거
    storedPages.pop();

    // 세션 스토리지 업데이트
    sessionStorage.setItem('pageStack', JSON.stringify(storedPages));

    // personal 데이터 받기
    var personal = document.querySelector('[data-personal]');

    if(personal){
        personal = personal.getAttribute('data-personal');
    }

    // 링크 이동 (이동 하게 되면 새로운 탭 링크가 추가 될 것)
    if(arg !== "default"){
        var newPath = `/${personal}/${arg}`;
    }
    else{
        var newPath = `/${personal}`;
    }
    // 새로운 URL로 이동
    window.location.href = newPath;
}