function createTag(input) {

    // 중복 체크
    var tagElements = document.querySelectorAll(".tag-box-content p"); // 현재 생성한 태그 값들의 이름들을 가져온다.

    var isDuplicate = Array.from(tagElements).some(function(tagElement) {
        return input.value.replace(/\s/g, '') === tagElement.textContent.replace(/\s/g, '');
    });
    if (isDuplicate) {
            var errorMessage = document.querySelector(".tagUpdateError");
            errorMessage.textContent = "중복된 태그가 있습니다";
            return;
    }


    // 생성
    var tagText = input.value.replace(/\s/g, '');
    if (tagText) {

      var newTag = document.createElement("div");
      newTag.className = 'tag-box-content';
      newTag.innerHTML = `<p>${tagText}</p>
      <input type="hidden" value="${tagText}">
      <img src="/img/Button-X.png" alt="취소버튼" onclick="removeTag(this)">`;

      var tagsContainer = document.querySelector(".tag-box");
      tagsContainer.appendChild(newTag);

      input.value = "";
      document.querySelector(".tagUpdateError").textContent = "";
    }
  }
function handleKeyDown(event) {

// 한글이 두번 찍히는 것을 방지
if (event.isComposing) return

if (event.key === "Enter") {
  createTag(event.target);
}
}

function removeTag(button) {
    var tagBox = button.parentElement; // 버튼의 부모의 부모의 부모 요소인 tag-box
    tagBox.remove();
}

function sendUpdateData(){

    // 이미지 파일 가져오기
    var backgroundInput = document.getElementById('backgroundInput');
    var profileInput = document.getElementById('UserImageInput');

    var backgroundFile = backgroundInput.files[0];
    var profileFile = profileInput.files[0];

    // 회원 정보 전달
     var nickName = document.querySelector("#nickName").value;
     var introduce = document.querySelector("#introduce").value;

     // 태그 정보 전달
     var createTaglist = [];
     var tagElements = document.querySelectorAll(".tag-box-content p");
     tagElements.forEach(function(tagElement){
        var createTag ={
            tagName: tagElement.textContent.replace(/\s/g, ''),
            tagCount: 0,
            weekCount: 0
        }
        createTaglist.push(createTag);
     });

    const formData = new FormData();
    formData.append('backgroundImg', backgroundFile);
    formData.append('profileImg', profileFile);
    formData.append('nickName', nickName);
    formData.append('introduce', introduce);

    createTaglist.forEach((tag,index) => {
            formData.append(`createTaglist[${index}].tagName`, tag.tagName);
            formData.append(`createTaglist[${index}].tagCount`, tag.tagCount);
            formData.append(`createTaglist[${index}].weekCount`, tag.weekCount);
        });

     fetch('/members/update', {
        method : 'POST',
        body : formData,
     })
     .then(response => {
        // 성공적으로 업데이타 이루어진 경우
         var currentUrl = window.location.href; // 현재 주소 불러오기
         var partToRemove = '/settings/profile'; // 업데이트 창 나가기 위해 url 지우기
         var moveUrl = currentUrl.replace(partToRemove, '');
         goBack();
     })
     .catch(error => {
        console.error("오류 발생 : ", error);
     });
}

