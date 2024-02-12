function previewImage() {
    // HTML에서 'imageInput'과 'image-preview'라는 ID를 가진 요소를 가져옴
    var input = document.getElementById('imageInput');
    var preview = document.getElementById('image-preview');

    // 선택된 파일이 없거나 이미지가 아닌 경우 리턴
    if (!input.files || input.files.length === 0 || !input.files[0].type.match('image.*')) {
        return;
    }
    // FileReader 객체를 생성
    var reader = new FileReader();

    // FileReader 객체의 onload 이벤트 핸들러를 정의
    reader.onload = function (e) {
        // 이미지를 미리보기로 표시
        preview.innerHTML = '<img src="' + e.target.result + '" class="preview-image" alt="Image Preview" /> <img class="cancel-image" src="/img/cancel.png" onclick="cancelImage()" alt="Image cancel" />';

    };

    // FileReader를 사용하여 선택된 파일을 읽어옴
    reader.readAsDataURL(input.files[0]);
 }



/* 글쓰기 이미지 삭제 */
 function cancelImage(){
    var input = document.getElementById('imageInput');
    var preview = document.getElementById('image-preview');


    // input 안에 들어가 있는 것을 없앤다.
    input.value = '';
    preview.innerHTML = '';

 }


/* 프로필 수정 */

// 이미지 변경하기 (백그라운드)
function profileBackgroundImg(){

    // HTML에서 'imageInput'과 'image-preview'라는 ID를 가진 요소를 가져옴
    var input = document.getElementById('backgroundInput');
    var background = document.getElementById('backgroundImg');

    // 선택된 파일이 없거나 이미지가 아닌 경우 리턴
        if (!input.files || input.files.length === 0 || !input.files[0].type.match('image.*')) {
            return;
        }


    // FileReader 객체를 생성
    var reader = new FileReader();

    reader.onload = function (e) {
            // 이미지를 미리보기로 표시
            background.src = e.target.result;
        };

        // FileReader를 사용하여 선택된 파일을 읽어옴
        reader.readAsDataURL(input.files[0]);
}


// 이미지 변경하기 (프로필)
function profileUserImg(){
 // HTML에서 'imageInput'과 'image-preview'라는 ID를 가진 요소를 가져옴
    var input = document.getElementById('UserImageInput');
    var profile = document.getElementById('profileImg');

    // 선택된 파일이 없거나 이미지가 아닌 경우 리턴
        if (!input.files || input.files.length === 0 || !input.files[0].type.match('image.*')) {
            return;
        }


    // FileReader 객체를 생성
    var reader = new FileReader();

    reader.onload = function (e) {
            // 이미지를 미리보기로 표시
            profile.src = e.target.result;
        };

        // FileReader를 사용하여 선택된 파일을 읽어옴
        reader.readAsDataURL(input.files[0]);
}