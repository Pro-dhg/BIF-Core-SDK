"use strict";

function queryById(id) {                        //合约内部函数
    let data = Chain.load(id);
    return data;
}

function query(input) {                         //合约查询入口
    input = JSON.parse(input);
    let id = input.id;
    let object = queryById(id);
    return object;
}

function main(input) {                          //合约调用入口
    input = JSON.parse(input);
    Chain.store(input.id, input.data);
}

function init(input) {                          //初始化函数
    return;
}