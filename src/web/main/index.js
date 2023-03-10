console.log("test");
addDiv("1", "222");

function addDiv(text1, text2) {
    let element = document.createElement('div');
    element.innerHTML = "<div style=\"background-color:blue;\">" + text1 + "</div><div>" + text2 + "</div>";
    document.body.appendChild(element);
}

let arr1 = [(1, "hi"), (2, "hi2"), (3, "hi3")];
arr1.forEach(addDiv)

for (var i = 0; i < 5; i++) {
    console.log(i + " dfakljsd");
}
console.log("a")

//asjdhadhlhdajdhjhlasdfhasjdhf
export function apiCall(route, body = {}. method = 'post') {
    const request = new Promise((resolve, reject) => {
        const headers = new Headers({
            'Content-Type': 'application/json'
        })
    });

    const requestDetails = { method, mode: 'cors', headers,};

    if (method !== 'GET') {requestDetails.body = JSON.stringify(body);}
}