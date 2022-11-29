'use strict';
const VERIFIABLE_CLAIM='verifiable_claim';
const IDENTITY='identity';
const OWNER='owner';

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

function issue(inputObj){
    let entity = inputObj.entity;
    let key = getKey(VERIFIABLE_CLAIM, entity.id);

    let oldEntity = loadObj(key);
    Utils.assert(oldEntity === false, 'already exists');

    let owner = loadObj(OWNER);
    let caller = Chain.msg.initiator;
    Utils.assert(caller === owner, 'no permission');

    saveObj(key,entity);

    let keyOfIdentity = getKey(IDENTITY, entity.subject);
    let identity = loadObj(keyOfIdentity);
    if(!identity){
        identity = [];
    }
    identity.push(entity.id);
    saveObj(keyOfIdentity, identity);
    Chain.tlog('identity', 'the verifiable claim was issued', keyOfIdentity);
}

function revoke(inputObj){
    let id = inputObj.id;
    let key = getKey(VERIFIABLE_CLAIM,id);
    let entity = loadObj(key);
    Utils.assert(entity !== false, 'not found');

    let caller = Chain.msg.initiator;
    Utils.assert(entity.subject === caller, 'no permission');

    delObj(key);

    let keyOfIdentity = getKey(IDENTITY, entity.subject);
    let identity = loadObj(keyOfIdentity);
    identity = identity.filter(function(value, index, arr){
        return value !== id;
    });
    saveObj(keyOfIdentity, identity);
}

function get(id){
    let result = loadObj(getKey(VERIFIABLE_CLAIM,id));
    return result;
}

function findBy(id){
    let vcs = [];
    let identity = loadObj(getKey(IDENTITY, id));
    if(!identity){
        return vcs;
    }
    let idx = 0;
    for(idx =0; idx < identity.length; idx+=1){
        let nid = identity[idx];
        let vc = loadObj(getKey(VERIFIABLE_CLAIM, nid));
        vcs.push(vc);
    }
    return vcs;
}

function query(input){
    let result={};
    let inputObj=JSON.parse(input);
    if(inputObj.method==='get') {
        result=get(inputObj.params.id);
    }
    else if(inputObj.method==='findBy') {
        result=findBy(inputObj.params.id);
    }else{
        result = false;
    }
    return JSON.stringify(result);
}

function init(input){
    let inputObj=JSON.parse(input);
    let owner = inputObj.owner;
    saveObj(OWNER, owner);
    return true;
}

function main(input){
    let funcList= {'issue':issue,'revoke':revoke,'get':get, 'findBy':findBy};
    let inputObj=JSON.parse(input);
    Utils.assert(funcList.hasOwnProperty(inputObj.method) && typeof funcList[inputObj.method]==='function','Cannot find func:'+inputObj.method);
    funcList[inputObj.method](inputObj.params);
}