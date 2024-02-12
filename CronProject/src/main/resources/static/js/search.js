function sendTagData(element){
    // 선택한 태그 이름 가져오기
    var tagName = element.querySelector('.selectTagName').textContent;
    // 선택한 고유 ID 가져오기
    var personal = element.querySelector('.selectPersonalName').textContent;
    document.getElementById('searchField').value = tagName + ' ' + personal;
    document.forms[0].submit();

}

function clickTagData(event){

    var tagName = event.currentTarget.querySelector('.selectTagName').textContent;
    var personal = event.currentTarget.querySelector('.selectPersonalName').textContent;

    var searchField = encodeURIComponent(`${tagName} ${personal}`);
    var url = `/search/result?searchField=${searchField}`

    window.location.href = url;
    event.stopPropagation();
}