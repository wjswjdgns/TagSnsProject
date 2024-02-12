    const textarea = document.getElementById('writePost');

    textarea.addEventListener('input', function () {
        this.style.height = 'auto'; // 높이 초기화
        this.style.height = (this.scrollHeight) + 'px'; // 내용에 따라 높이 조절
    });


    document.getElementById('writePost').addEventListener('input', function (e) {
        let content = this.value;

        // 글자수 세기
        let charCount = content.length;

        // 글자수 표시 업데이트
        document.querySelector('.textCount').textContent = charCount;

        // 글자수 제한
        const maxLength = 160;
        if (charCount > maxLength) {
            // 160자부터는 타이핑 되지 않도록
            this.value = content.substring(0, maxLength);
            // 160자를 넘으면 알림창이 뜨도록
            alert('글자수는 160자까지 입력 가능합니다.');
            // 제한 이후의 글자 수 업데이트
            document.querySelector('.textCount').textContent = maxLength;
        }
    });


// Post 등록
function sendPostData(){

 // 포스트 등록
   if(window.location.pathname == "/compose/default"){

         // 이미지 파일 가져오기
         var input = document.getElementById('imageInput');
         var file = input.files[0]; // input에 담겨진 파일을 가져온다

         var writePost = document.querySelector("#writePost").value;

         if(document.querySelector(".selectTagData") == null){
            var tagId = null;
         }
         else{
            var tagId = parseInt(document.querySelector(".selectTagData").getAttribute('Data-selectTagId'),10);
         }

        const formData = new FormData();
        formData.append('image', file);
        formData.append('content', writePost);
        formData.append('tagId', tagId !== null ? tagId : '');

        // 정상적으로 값이 들어왔는지 확인하는 코드
        for(let key of formData.keys()){
            console.log(key, ":", formData.get(key));
        }

         fetch('/write/default', {
            method : 'POST',
            body : formData,
         })
         .then(response => {
            if(response.ok){
                goBack();
            }
            else{
                console.log('포스트 업로드에 실패했습니다.');
            }
         })
         .catch(error => {
            console.error("오류 발생 : ", error);
         });
   }

// 답글 등록
   if(window.location.pathname == "/compose/comment"){

         // 이미지 파일 가져오기
         var input = document.getElementById('imageInput');
         var file = input.files[0]; // input에 담겨진 파일을 가져온다

         var writePost = document.querySelector("#writePost").value;
         var postId = null;
         var commentId = null;

         if(document.querySelector(".prev-content").getAttribute('data-post-check') === "true"){
            postId = parseInt(document.querySelector(".prev-content").getAttribute('data-post-id'),10);
         }
         else{
            commentId = parseInt(document.querySelector(".prev-content").getAttribute('data-post-id'),10);
         }

         const formData = new FormData();
         formData.append('image', file);
         formData.append('content', writePost);
         formData.append('Par_postId', postId !== null ? postId : '');
         formData.append('Par_commentId', commentId !== null ? commentId : '');


         // 정상적으로 값이 들어왔는지 확인하는 코드
         for(let key of formData.keys()){
             console.log(key, ":", formData.get(key));
         }


         fetch('/write/comment', {
            method : 'POST',
            body : formData,
         })
         .then(response => {
            goBack();
         })
         .catch(error => {
            console.error("오류 발생 : ", error);
         });
   }

}

// 포스트 수정
  function EditPostData(){

         // 이미지 파일 가져오기
          var input = document.getElementById('imageInput');
          var file = input.files[0]; // input에 담겨진 파일을 가져온다
          var checkImage = document.querySelector(".preview-image");
          console.log("이미지가 있으면 true 없으면 false " + checkImage);

          var writePost = document.querySelector("#writePost").value;

          var postId = document.querySelector('.editPostId').value;
          var postCheck = document.querySelector('.editPostCheck').value;

          if(document.querySelector(".selectTagData") == null){
             var tagId = null;
          }
          else{
             var tagId = parseInt(document.querySelector(".selectTagData").getAttribute('Data-selectTagId'),10);
          }

         const formData = new FormData();
         formData.append('image', file);
         formData.append('content', writePost);
         formData.append('tagId', tagId !== null ? tagId : '');

         // 정상적으로 값이 들어왔는지 확인하는 코드
         for(let key of formData.keys()){
             console.log(key, ":", formData.get(key));
         }

         formData.append('postId', postId);
         formData.append('postCheck',postCheck);

         if(checkImage != null){
            formData.append('checkImage', 'true');
            console.log("true 값이 들어갔습니다.");
         }
         else{
            formData.append('checkImage', 'false');
            console.log("false 값이 들어갔습니다.");
         }


         fetch('/write/edit', {
             method : 'POST',
             body : formData,
          })
          .then(response => {
             if(response.ok){
                 goBack();
             }
             else{
                 console.log('포스트 업로드에 실패했습니다.');
             }
          })
          .catch(error => {
             console.error("오류 발생 : ", error);
          });
  }

// 팝업 오픈
function popup(){
    var tagElements = document.querySelector(".tagPopup.deactive");

    if(tagElements){
        tagElements.classList.remove("deactive");
        tagElements.classList.add("active");
    }
}

// 팝업 취소
function popupCancel(){
    var tagElements = document.querySelector(".tagPopup.active");

    if(tagElements){
        tagElements.classList.remove("active");
        tagElements.classList.add("deactive");
    }
}

// 팝업 태그 클릭
function clickTag(element){

       // Post 형식으로 데이터를 fetch api를 통해 전달해서 답을 가져온다.
       var selectTagId = parseInt(element.querySelector(".tag-area").getAttribute('data-tag-id'),10)
       var selectTagName = element.querySelector(".tag-area").textContent
       var SelectMemberPersonal = element.closest(".UserTagContent").querySelector(".user-id").textContent


      // 클릭 시 .selectTag 생성
        let writeWord = document.querySelector(".write-word");

        let obj = document.createElement('div');
        obj.setAttribute('class', 'selectTag')

        let f = document.createElement('p');
        f.setAttribute('class', 'selectTagData');
        f.setAttribute("Data-selectTagId", selectTagId);
        f.setAttribute("Data-selectTagName", selectTagName);
        f.setAttribute("Data-selectMemberPersonal", SelectMemberPersonal);

        let s1 = document.createElement('span');
        s1.setAttribute('class', 'font-middle-w selectTagName');
        s1.innerText=selectTagName;

        let s2 = document.createElement('span');
        s2.setAttribute('class', 'font-small selectPersonalMember');
        s2.innerText=SelectMemberPersonal;

        f.appendChild(s1);
        f.appendChild(s2);
        obj.appendChild(f);

        writeWord.insertBefore(obj, writeWord.firstChild);

        // #태그 지정을 사라지게 만든다.
        document.querySelector(".write-word-tag.activeTag").style.display = 'none';
        document.querySelector(".write-word-tag.deactiveTag").style.display = 'inline-block';


        // 클릭을 하는 순간 태그 창이 닫히면서 데이터를 전달을 해서 값을 바꿔야 한다.
        var tagElements = document.querySelector(".tagPopup.active");

        if(tagElements){
            tagElements.classList.remove("active");
            tagElements.classList.add("deactive");
        }
}

// 태그 취소
function cancelTag(element){
     document.querySelector(".write-word-tag.activeTag").style.display = 'inline-block';
     document.querySelector(".write-word-tag.deactiveTag").style.display = 'none';
     var selectTag = document.querySelector(".selectTag");
     selectTag.remove();
}


