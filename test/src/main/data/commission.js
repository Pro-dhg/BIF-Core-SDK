'use strict';
const OWNER="owner";
const COMMISSION='commission';
const COMMISSIONER='commissioner';

function loadObj(key){
    let data=Chain.load(key);
    return JSON.parse(data);
}

function saveObj(key,value) {
    Chain.store(key,JSON.stringify(value));
}

function delObj(key) {
    Chain.del(key);
}

function getKey(first,second,third=''){
    return(third==='')? (first+'_'+second):(first+'_'+second+'_'+third);
}

function get(){
    let result = loadObj(COMMISSION);
    return result;
}

function getMember(id){
    let result = loadObj(getKey(COMMISSIONER,id));
    return result;
}

function addMember(inputObj){
    let entity = inputObj.entity;
    let key = getKey(COMMISSIONER, entity.id);

    let oldEntity = loadObj(key);
    Utils.assert(oldEntity === false, 'already exists');

    let owner = loadObj(OWNER);
    let caller = Chain.msg.initiator;
    Utils.assert(caller === owner, 'no permission');

    saveObj(key, entity);
    let commission = loadObj(COMMISSION);
    commission.members.push(entity);
    saveObj(COMMISSION, commission);
    Chain.tlog('commission', 'the member joined successfully');
}

function updateMemberInfo(inputObj){
    let entity = inputObj.entity;
    let key = getKey(COMMISSIONER, entity.id);
    let oldEntity = loadObj(key);
    Utils.assert(oldEntity !== false, 'not found');
    saveObj(key, entity);
    let commission = loadObj(COMMISSION);
    commission.members.push(entity);
    saveObj(COMMISSION, commission);
    Chain.tlog('commission', 'the member updated successfully');
}

function expelMember(inputObj){
    let id = inputObj.id;
    let key = getKey(COMMISSIONER, id);
    let oldEntity = loadObj(key);
    Utils.assert(oldEntity !== false, 'not found');
    delObj(key);

    let commission = loadObj(COMMISSION);
    let members = commission.members;
    members = members.filter(function(value, index, arr){
            return value.id !== id;
    });
    commission.members = members;
    saveObj(COMMISSION, commission);
    Chain.tlog('commission', 'the member was expelled successfully');
}

function query(input){
    let result={};
    let inputObj=JSON.parse(input);
    if(inputObj.method==='getMember') {
        result=getMember(inputObj.params.id);
    }else{
        result=get();
    }
    return JSON.stringify(result);
}

function init(input){
    let inputObj=JSON.parse(input);
    let commission = inputObj.commission;
    let members = inputObj.members;
    let owner = inputObj.owner;

    let i = 0;
    for(i =0; i < members.length; i+=1){
        let member = members[i];
        member.createdAt = Chain.block.timestamp;
        saveObj(getKey(COMMISSIONER, member.id), member);
    }
    saveObj(OWNER, owner);
    saveObj(COMMISSION, commission);
    return true;
}

function main(input){
    let funcList= {'get':get,'getMember':getMember, 'addMember':addMember, 'updateMemberInfo':updateMemberInfo, 'expelMember':expelMember};
    let inputObj=JSON.parse(input);
    Utils.assert(funcList.hasOwnProperty(inputObj.method) &&typeof funcList[inputObj.method]==='function','Cannot find func:'+inputObj.method);
    funcList[inputObj.method](inputObj.params);
}