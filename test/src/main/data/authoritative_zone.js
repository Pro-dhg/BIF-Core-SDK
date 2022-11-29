'use strict';
const ADMINISTRATOR="administrator";
const OWNER="owner";
const AUTHORITATIVE_ZONE='authoritative_zone';
const AUTHORITY='authority';
const ALL_AUTHORITY_IDS='all_authority_ids';
const DELEGATION='delegation';
const ALL_DELEGATION_IDS='all_delegation_ids';

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

function addIdentifier(inputObj) {
    let entity = inputObj.entity;
    let isDelegation = inputObj.isDelegation;
    let isTrusteeship = inputObj.isTrusteeship;
    let identifierKey = isDelegation ? DELEGATION : AUTHORITY;
    let key = getKey(identifierKey, entity.id);

    let oldEntity = loadObj(key);
    Utils.assert(oldEntity === false, 'already exists');

    if (isTrusteeship) {
        Utils.assert(Chain.msg.initiator === loadObj(ADMINISTRATOR), 'no permission');
    } else {
        Utils.assert(Chain.msg.initiator === loadObj(OWNER), 'no permission');
    }

    saveObj(key, entity);

    let idsKey = isDelegation ? ALL_DELEGATION_IDS : ALL_AUTHORITY_IDS;
    let identifier_ids = loadObj(idsKey);
    identifier_ids.push(entity.id);
    saveObj(idsKey, identifier_ids);
    Chain.tlog('authoritative_zone', 'the authoritative zone identifier was added successfully');
}

function updateIdentifier(inputObj){
     let entity = inputObj.entity;
     let isTrusteeship = inputObj.isTrusteeship;
     let isDelegation = inputObj.isDelegation;
     let identifierKey = isDelegation ? DELEGATION : AUTHORITY;
     let key = getKey(identifierKey, entity.id);

     let oldEntity = loadObj(key);
     Utils.assert(oldEntity !== false, 'not found');

     if (isTrusteeship) {
        Utils.assert(Chain.msg.initiator === loadObj(ADMINISTRATOR), 'no permission');
     } else {
        Utils.assert(Chain.msg.initiator === loadObj(OWNER), 'no permission');
     }

     saveObj(key, inputObj.entity);

     Chain.tlog('authoritative_zone', 'the authoritative zone identifier has been updated successfully');
}

function deleteIdentifier(inputObj){
    let entity = inputObj.entity;
    let isTrusteeship = inputObj.isTrusteeship;
    let isDelegation = inputObj.isDelegation;
    let identifierKey = isDelegation ? DELEGATION : AUTHORITY;
    let key = getKey(identifierKey, entity.id);

    if (isTrusteeship) {
        Utils.assert(Chain.msg.initiator === loadObj(ADMINISTRATOR), 'no permission');
    } else {
        Utils.assert(Chain.msg.initiator === loadObj(OWNER), 'no permission');
    }

    let oldEntity = loadObj(key);
    Utils.assert(oldEntity !== false, 'not found');

    delObj(key);

    let idsKey = isDelegation ? ALL_DELEGATION_IDS : ALL_AUTHORITY_IDS;
    let identifier_ids = loadObj(idsKey);
    identifier_ids = identifier_ids.filter(function(value, index, arr){
        return value !== entity.id;
    });
    saveObj(idsKey, identifier_ids);

    Chain.tlog('authoritative_zone', 'the authoritative zone identifier was revoked successfully');
}

function getIdentifier(id){
    let result = loadObj(getKey(AUTHORITY, id));
    if (!result) {
        return loadObj(getKey(DELEGATION, id));
    }
    return result;
}

function getIdentifiers(){
    let identifiers = [];
    let aIds = loadObj(ALL_AUTHORITY_IDS);
    let aIdx = 0;
    for(aIdx =0; aIdx < aIds.length; aIdx+=1){
        let aid = aIds[aIdx];
        identifiers.push(loadObj(getKey(AUTHORITY, aid)));
    }
    let dIds = loadObj(ALL_DELEGATION_IDS);
    let dIdx = 0;
    for(dIdx =0; dIdx < dIds.length; dIdx+=1){
        let did = dIds[dIdx];
        identifiers.push(loadObj(getKey(DELEGATION, did)));
    }
    return identifiers;
}

function updateAuthoritativeZone(inputObj){
     let authoritativeZone = loadObj(AUTHORITATIVE_ZONE);

     Utils.assert(authoritativeZone !== false, 'not found');

     let owner = loadObj(OWNER);
     Utils.assert(Chain.msg.initiator === owner, 'no permission');

     authoritativeZone = inputObj.entity;
     saveObj(AUTHORITATIVE_ZONE, authoritativeZone);

     if (inputObj.changeOwner) {
        saveObj(OWNER, authoritativeZone.owner);
        let aIds = loadObj(ALL_AUTHORITY_IDS);
        let aIdx = 0;
        for(aIdx =0; aIdx < aIds.length; aIdx+=1){
            let aid = aIds[aIdx];
            let aIdentifier = loadObj(getKey(AUTHORITY, aid));
            aIdentifier.owner = authoritativeZone.owner;
            saveObj(getKey(AUTHORITY, aid), aIdentifier);
        }
        let dIds = loadObj(ALL_DELEGATION_IDS);
        let dIdx = 0;
        for(dIdx =0; dIdx < dIds.length; dIdx+=1){
            let did = dIds[dIdx];
            let dIdentifier = loadObj(getKey(DELEGATION, did));
            dIdentifier.owner = authoritativeZone.owner;
            saveObj(getKey(DELEGATION, did), dIdentifier);
        }
     }
     Chain.tlog('authoritative_zone', 'the authoritative zone has been updated');
}

function getAuthoritativeZone(){
    let result = loadObj(AUTHORITATIVE_ZONE);
    return result;
}

function deleteAuthoritativeZone(){
    let owner = loadObj(OWNER);
    let caller = Chain.msg.initiator;
    Utils.assert(caller === owner, 'no permission');

    let entity = loadObj(AUTHORITATIVE_ZONE);
    Utils.assert(entity !== false, 'not found');

    let aIds = loadObj(ALL_AUTHORITY_IDS);
    let aIdx = 0;
    for(aIdx =0; aIdx < aIds.length; aIdx+=1){
        let aid = aIds[aIdx];
        delObj(getKey(AUTHORITY, aid));
    }
    let dIds = loadObj(ALL_DELEGATION_IDS);
    let dIdx = 0;
    for(dIdx =0; dIdx < dIds.length; dIdx+=1){
        let did = dIds[dIdx];
        delObj(getKey(DELEGATION, did));
    }

    let emptyIds = [];
    saveObj(ALL_AUTHORITY_IDS, emptyIds);
    saveObj(ALL_DELEGATION_IDS, emptyIds);
    delObj(AUTHORITATIVE_ZONE);
}

function query(input){
    let result={};
    let inputObj=JSON.parse(input);
    if(inputObj.method==='getIdentifier'){
        result=getIdentifier(inputObj.params.id);
    }else if(inputObj.method==='getIdentifiers'){
        result=getIdentifiers();
    }else if(inputObj.method==='getAuthoritativeZone'){
        result=getAuthoritativeZone();
    }else{
        result=false;
    }
    return JSON.stringify(result);
}

function init(input){
    let inputObj=JSON.parse(input);
    let administrator = inputObj.administrator;
    let authoritativeZone = inputObj.authoritativeZone;
    let authority_ids = [];
    let delegation_ids = [];
    saveObj(ADMINISTRATOR, administrator);
    saveObj(AUTHORITATIVE_ZONE, authoritativeZone);
    saveObj(OWNER, authoritativeZone.owner);
    saveObj(ALL_AUTHORITY_IDS, authority_ids);
    saveObj(ALL_DELEGATION_IDS, delegation_ids);
    return true;
}

function main(input){
    let funcList= {'addIdentifier':addIdentifier,'updateIdentifier':updateIdentifier,'deleteIdentifier':deleteIdentifier,
    'getIdentifier':getIdentifier,'getIdentifiers':getIdentifiers,
    'updateAuthoritativeZone':updateAuthoritativeZone,'deleteAuthoritativeZone':deleteAuthoritativeZone,
    'getAuthoritativeZone':getAuthoritativeZone};
    let inputObj=JSON.parse(input);
    Utils.assert(funcList.hasOwnProperty(inputObj.method) &&typeof funcList[inputObj.method]==='function','Cannot find func:'+inputObj.method);
    funcList[inputObj.method](inputObj.params);
}