"use strict";

function query(input) {
    return Chain.contractQuery("did:bid:ef8TqstyTi5uggUX15V1Sj9ntRz6bK2w",input);
}
function main(input) {
    input = JSON.parse(input);
    Chain.store(input.id, input.data);
}
function init(input) {
    return;
}