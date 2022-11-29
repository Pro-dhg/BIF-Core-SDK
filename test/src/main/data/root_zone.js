'use strict';
const OWNER="owner";
const AUTHORITATIVE_ZONE='authoritative_zone';
const ROOT_ZONE='root_zone';
const ALL_AUTHORITATIVE_ZONE_IDS='all_authoritative_ids';
const IDENTIFIER='identifier';
const ALL_IDENTIFIER_IDS='all_identifier_ids';

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
    let key = getKey(IDENTIFIER, entity.id);

    let oldEntity = loadObj(key);
    Utils.assert(oldEntity === false, 'already exists');

    let owner = loadObj(OWNER);
    let caller = Chain.msg.initiator;
    Utils.assert(caller === owner, 'no permission');

    saveObj(key, entity);

    let identifier_ids = loadObj(ALL_IDENTIFIER_IDS);
    identifier_ids.push(entity.id);
    let rootZone = loadObj(ROOT_ZONE);
    rootZone.serial = rootZone.serial +1;
    saveObj(ALL_IDENTIFIER_IDS, identifier_ids);
    saveObj(ROOT_ZONE, rootZone);
    Chain.tlog('root_zone', 'the root zone identifier was added successfully');
}

function updateIdentifier(inputObj){
     let id = inputObj.entity.id;
     let key = getKey(IDENTIFIER, id);

     let entity = loadObj(key);
     Utils.assert(entity !== false, 'not found');

     let owner = loadObj(OWNER);
     Utils.assert(Chain.msg.initiator === owner, 'no permission');

     saveObj(key, inputObj.entity);

     let rootZone = loadObj(ROOT_ZONE);
     rootZone.serial = rootZone.serial +1;
     saveObj(ROOT_ZONE, rootZone);
     Chain.tlog('root_zone', 'the root zone identifier has been updated successfully');
}

function deleteIdentifier(inputObj){
    let id = inputObj.id;

    let owner = loadObj(OWNER);
    let caller = Chain.msg.initiator;
    Utils.assert(caller === owner, 'no permission');

    let entity = loadObj(getKey(IDENTIFIER, id));
    Utils.assert(entity !== false, 'not found');

    delObj(getKey(IDENTIFIER, id));

    let identifier_ids = loadObj(ALL_IDENTIFIER_IDS);
    identifier_ids = identifier_ids.filter(function(value, index, arr){
        return value !== id;
    });
    let rootZone = loadObj(ROOT_ZONE);
    rootZone.serial = rootZone.serial +1;
    saveObj(ROOT_ZONE, rootZone);
    saveObj(ALL_IDENTIFIER_IDS, identifier_ids);

    Chain.tlog('root_zone', 'the root zone identifier was revoked successfully');
}

function addRootZoneRecord(inputObj){
    let entity = inputObj.entity;
    let key = getKey(AUTHORITATIVE_ZONE, entity.id);

    let oldEntity = loadObj(key);
    Utils.assert(oldEntity === false, 'already exists');

    let owner = loadObj(OWNER);
    let caller = Chain.msg.initiator;
    Utils.assert(caller === owner, 'no permission');

    saveObj(key, entity);

    let namespace_ids = loadObj(ALL_AUTHORITATIVE_ZONE_IDS);
    namespace_ids.push(entity.id);
    let rootZone = loadObj(ROOT_ZONE);
    rootZone.serial = rootZone.serial +1;
    saveObj(ALL_AUTHORITATIVE_ZONE_IDS, namespace_ids);
    saveObj(ROOT_ZONE, rootZone);
    Chain.tlog('root_zone', 'the root zone record was added successfully');
}

function deleteRootZoneRecord(inputObj){
    let id = inputObj.id;

    let owner = loadObj(OWNER);
    let caller = Chain.msg.initiator;
    Utils.assert(caller === owner, 'no permission');

    let entity = loadObj(getKey(AUTHORITATIVE_ZONE, id));
    Utils.assert(entity !== false, 'not found');

    delObj(getKey(AUTHORITATIVE_ZONE, id));

    let namespace_ids = loadObj(ALL_AUTHORITATIVE_ZONE_IDS);
    let rootZone = loadObj(ROOT_ZONE);
    namespace_ids = namespace_ids.filter(function(value, index, arr){
        return value !== id;
    });
    rootZone.serial = rootZone.serial +1;
    saveObj(ROOT_ZONE, rootZone);
    saveObj(ALL_AUTHORITATIVE_ZONE_IDS, namespace_ids);

    Chain.tlog('root_zone', 'the root zone record was revoked successfully');
}

function updateRootZoneRecord(inputObj){
     let id = inputObj.entity.id;
     let key = getKey(AUTHORITATIVE_ZONE, id);

     let entity = loadObj(key);
     Utils.assert(entity !== false, 'not found');

     let owner = loadObj(OWNER);
     Utils.assert(Chain.msg.initiator === owner, 'no permission');

     saveObj(key, inputObj.entity);

     let rootZone = loadObj(ROOT_ZONE);
     rootZone.serial = rootZone.serial +1;
     saveObj(ROOT_ZONE, rootZone);
     Chain.tlog('root_zone', 'the root zone record has been updated successfully');
}

function updateRootZone(inputObj){
     let rootZone = loadObj(ROOT_ZONE);

     let owner = loadObj(OWNER);
     Utils.assert(Chain.msg.initiator === owner, 'no permission');

     rootZone = inputObj.entity;
     rootZone.serial = rootZone.serial +1;
     saveObj(ROOT_ZONE, rootZone);
     Chain.tlog('root_zone', 'the root zone has been updated');
}

function getRootZone(){
    let result = loadObj(ROOT_ZONE);
//    result.authoritativeZones = getRootZoneRecords();
//    result.rrs = getIdentifiers();
    return result;
}

function getRootZoneRecord(id){
    let result = loadObj(getKey(AUTHORITATIVE_ZONE,id));
    return result;
}

function getIdentifier(id){
    let result = loadObj(getKey(IDENTIFIER,id));
    return result;
}

function getRootZoneRecords(){
    let namespace_ids = loadObj(ALL_AUTHORITATIVE_ZONE_IDS);
    if(!namespace_ids){
        return false;
    }
    let namespaces = [];
    let idx = 0;
    for(idx =0; idx < namespace_ids.length; idx+=1){
        let nid = namespace_ids[idx];
        let namespace = loadObj(getKey(AUTHORITATIVE_ZONE, nid));
        namespaces.push(namespace);
    }
    return namespaces;
}

function getIdentifiers(){
    let identifier_ids = loadObj(ALL_IDENTIFIER_IDS);
    if(!identifier_ids){
        return false;
    }
    let identifiers = [];
    let idx = 0;
    for(idx =0; idx < identifier_ids.length; idx+=1){
        let nid = identifier_ids[idx];
        let identifier = loadObj(getKey(IDENTIFIER, nid));
        identifiers.push(identifier);
    }
    return identifiers;
}

function query(input){
    let result={};
    let inputObj=JSON.parse(input);
    if(inputObj.method==='getRootZoneRecord') {
        result=getRootZoneRecord(inputObj.params.id);
    }else if(inputObj.method==='getRootZoneRecords'){
        result=getRootZoneRecords();
    }else if(inputObj.method==='getIdentifier'){
        result=getIdentifier(inputObj.params.id);
    }else if(inputObj.method==='getIdentifiers'){
        result=getIdentifiers();
    }else if(inputObj.method==='getRootZone'){
        result=getRootZone();
    }else{
        result=false;
    }
    return JSON.stringify(result);
}

function init(input){
    let inputObj=JSON.parse(input);
    let rootZone = inputObj.rootZone;
    let namespace_ids = [];
    let identifier_ids = [];
    saveObj(ROOT_ZONE, rootZone);
    saveObj(OWNER, inputObj.owner);
    saveObj(ALL_AUTHORITATIVE_ZONE_IDS, namespace_ids);
    saveObj(ALL_IDENTIFIER_IDS, identifier_ids);
    return true;
}

function main(input){
    let funcList= {'addIdentifier':addIdentifier,'updateIdentifier':updateIdentifier,'deleteIdentifier':deleteIdentifier,
    'addRootZoneRecord':addRootZoneRecord,'deleteRootZoneRecord': deleteRootZoneRecord,
    'getRootZone':getRootZone, 'getRootZoneRecord':getRootZoneRecord,'getRootZoneRecords':getRootZoneRecords,
    'getIdentifier':getIdentifier,'getIdentifiers':getIdentifiers,
    'updateRootZoneRecord':updateRootZoneRecord, 'updateRootZone':updateRootZone};
    let inputObj=JSON.parse(input);
    Utils.assert(funcList.hasOwnProperty(inputObj.method) &&typeof funcList[inputObj.method]==='function','Cannot find func:'+inputObj.method);
    funcList[inputObj.method](inputObj.params);
}