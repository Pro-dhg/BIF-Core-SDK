'use strict';
const VERIFIABLE_CLAIM = 'verifiable_claim';
const IDENTITY = 'identity';
const OWNER = 'owner';
const Register_commission='register_commission';
const Register_commission_address='register_commission_address';
const Security_commission='security_commission';
const Security_commission_address='security_commission_address';

function loadObj(key) {
    let data = Chain.load(key);
    return JSON.parse(data);
}

function saveObj(key, value) {
    Chain.store(key, JSON.stringify(value));
}

function delObj(key) {
    Chain.del(key);
}

function getKey(first, second, third = '') {
    return (third === '') ? (first + '_' + second) : (first + '_' + second + '_' + third);
}

function isOkIncommissions(caller){
    let id = '' ;
    let reg = loadObj(Register_commission);
    let sec = loadObj(Security_commission);

    let regMembers = reg.members;
    let i = 0 ;
    for (i = 0; i < regMembers.length; i+=1) {
        let regMember = regMembers[i];
        if (caller === regMember.id) {
            id = regMember.id ;
        }
    }

    let secMembers = sec.members ;
    for (i = 0; i < secMembers.length; i+=1) {
        let secMember = secMembers[i];
        if (caller === secMember.id){
            id = secMember.id ;
        }
    }
    Utils.assert(caller === id, 'no permission');

}

function issue(inputObj) {
    let entity = inputObj.entity;
    let key = getKey(VERIFIABLE_CLAIM, entity.id);

    let oldEntity = loadObj(key);
    Utils.assert(oldEntity === false, 'already exists');

    let owner = loadObj(OWNER);
    let caller = Chain.msg.initiator;
    Utils.assert(caller === owner, 'no permission');

    saveObj(key, entity);

    let keyOfIdentity = getKey(IDENTITY, entity.subject);
    let identity = loadObj(keyOfIdentity);
    if (!identity) {
        identity = [];
    }
    identity.push(entity.id);
    saveObj(keyOfIdentity, identity);
    saveObj(owner, identity);
    Chain.tlog('identity', 'the verifiable claim was issued', keyOfIdentity);
}

function revoke(inputObj) {
    let id = inputObj.id;
    let key = getKey(VERIFIABLE_CLAIM, id);
    let entity = loadObj(key);
    Utils.assert(entity !== false, 'not found');

    let caller = Chain.msg.initiator;
    Utils.assert(entity.subject === caller, 'no permission');

    delObj(key);

    let keyOfIdentity = getKey(IDENTITY, entity.subject);
    let identity = loadObj(keyOfIdentity);
    identity = identity.filter(function (value, index, arr) {
        return value !== id;
    });
    saveObj(keyOfIdentity, identity);
    saveObj(loadObj(OWNER), identity);
}

function get() {
    let result = [
        {"register":loadObj(Register_commission)},
        {"security":loadObj(Security_commission)},
        {"identity":loadObj(getKey(IDENTITY, loadObj(OWNER)))}
    ];

    return result;
}
function getMember(inputObj) {
    let registerContractAddress = loadObj(Register_commission_address);
    let securityContractAddress = loadObj(Security_commission_address);

    Utils.assert(registerContractAddress !== false, 'no registerContractAddress,please initCommissions at first');
    Utils.assert(securityContractAddress !== false, 'no securityContractAddress,please initCommissions at first');

    let commissionInput = {"method": "getMember","params":{"id":inputObj.id}};

    let registerRes = Chain.contractQuery(registerContractAddress,JSON.stringify(commissionInput));

    let securityRes = Chain.contractQuery(securityContractAddress,JSON.stringify(commissionInput));

    let identityRes = loadObj(getKey(VERIFIABLE_CLAIM, inputObj.id));
    if (identityRes === undefined){
        identityRes='wu';
    }

    let result = [
        {"register":registerRes.result},
        {"security":securityRes.result},
        {"identity":identityRes.result}
    ] ;
    return result ;
}

function findBy(id) {
    let vcs = [];
    let identity = loadObj(getKey(IDENTITY, id));
    if (!identity) {
        return vcs;
    }
    let idx = 0;
    for (idx = 0; idx < identity.length; idx += 1) {
        let nid = identity[idx];
        let vc = loadObj(getKey(VERIFIABLE_CLAIM, nid));
        vcs.push(vc);
    }
    return vcs;
}

function initCommissions(inputObj){
    let owner = loadObj(OWNER);
    let caller = Chain.msg.sender;
    Utils.assert(caller === owner, 'no permission');

    if (inputObj.register_commission !== undefined){
    let registerContractAddress = inputObj.register_commission.contractAddress;
        Chain.contractCall(registerContractAddress,true,"0",JSON.stringify(inputObj.register_commission));
        saveObj(Register_commission_address,registerContractAddress);
    }

    if (inputObj.security_commission !== undefined){
    let securityContractAddress = inputObj.security_commission.contractAddress;
        Chain.contractCall(securityContractAddress,true,"0",JSON.stringify(inputObj.security_commission));
        saveObj(Security_commission_address,securityContractAddress);
    }

    Chain.tlog('commission', 'init commission is ok');
}

function init(input) {
    let inputObj = JSON.parse(input);
    let owner = inputObj.owner;
    saveObj(OWNER, owner);
    return true;
}

function query(input) {
    let result = {};
    let inputObj = JSON.parse(input);
    if (inputObj.method === 'get') {
        result = get(inputObj.params.id);
    }else if (inputObj.method === 'getMember') {
        result = getMember(inputObj.params);
    }else if (inputObj.method === 'findBy') {
        result = findBy(inputObj.params.id);
    }else if (inputObj.method === 'initCommissions') {
        result = initCommissions(inputObj.params);
    }else if (inputObj.method === 'isOkIncommissions') {
        result = isOkIncommissions(inputObj.params);
    } else {
        result = false;
    }
    return JSON.stringify(result);
}

function main(input) {
    let funcList = {'issue': issue, 'revoke': revoke, 'get': get, 'findBy': findBy,'initCommissions': initCommissions,'isOkIncommissions':isOkIncommissions,'getMember':getMember};
    let inputObj = JSON.parse(input);
    Utils.assert(funcList.hasOwnProperty(inputObj.method) && typeof funcList[inputObj.method] === 'function', 'Cannot find func:' + inputObj.method);
    funcList[inputObj.method](inputObj.params);
}