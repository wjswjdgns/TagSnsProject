
function allMain(element){
    location.href = '/';
}

function selectMain(element){

    var personal = element.querySelector('.memberPersonal').getAttribute('data-member-personal');

    let f = document.createElement('form');
    let obj;
    obj = document.createElement('input');
    obj.setAttribute('type', 'hidden');
    obj.setAttribute('name', 'personal');
    obj.setAttribute('value', personal);

    f.appendChild(obj);
    f.setAttribute('method', 'post');
    f.setAttribute('action', '/')
    document.body.appendChild(f);
    f.submit();
}


function logout(){
    fetch('/members/logout',{
        method: 'POST',
        headers:{
        'Content-Type': 'application/json'
        },
    })
     .then(response => {
        // 응답 처리
        if (response.ok) {
            // 성공적으로 로그아웃이 수행됐을 때
            CleanValue();
            window.location.href = '/'; // 홈페이지로 이동
        } else {
            // 로그아웃 실패 또는 다른 이유로 응답이 실패했을 때
            console.error('로그아웃 실패');
        }
    })
    .catch(error => {
        console.error('로그아웃 중 오류 발생:', error);
    });
}

function removeUser(){

    // 확인 팝업
    var isWithdrawalConfirmed = confirm("정말로 탈퇴하시겠습니까?");
    if (isWithdrawalConfirmed){
        fetch('/members/delete',{
                method: 'POST',
                headers:{
                'Content-Type': 'application/json'
                },
            })
             .then(response => {
                // 응답 처리
                if (response.ok) {
                    // 성공적으로 로그아웃이 수행됐을 때
                    logout();
                } else {
                    // 로그아웃 실패 또는 다른 이유로 응답이 실패했을 때
                    console.error('회원 탈퇴 실패');
                }
            })
            .catch(error => {
                console.error('회원 탈퇴 중 오류 발생:', error);
            });
    }
    else{
        return;
    }

}
