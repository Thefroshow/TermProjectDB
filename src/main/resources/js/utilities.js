var getValue = function (url,key) {

    var start = url.indexOf(key)+key.length+1;
    var end = url.indexOf("&",start);
    if(end == -1){
        end = url.length;
    }else{
        end-1;
    }
    var value = url.substr(start,end);
    return value;
};

var setStyleSheet = function (name) {

    var head  = document.getElementsByTagName('head')[0];
    var old =document.getElementById("link");
    if(old){
        head.removeChild(old);
    }

    var link  = document.createElement('link');
    link.id   = "link";
    link.rel  = 'stylesheet';
    link.type = 'text/css';
    link.href = 'css/'+name+'.css';
    link.media = 'all';
    head.appendChild(link);
};


var toRestaurant = function (name,rating,price,phone,urlString,image,address,state,zip,city) {

    var restaurant = {
        name: name,
        rating: rating,
        price: price,
        phone: phone,
        urlString: urlString,
        image: image,
        address: address,
        state: state,
        zip: zip,
        city: city
    };
    return restaurant;
};

