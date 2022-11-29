'use strict';
const POLL="poll";
const VOTE='vote';
const OWNER='owner';
const POLL_VOTES="poll_votes";

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

function createPoll(inputObj){
    let entity = inputObj.entity;
    let key = getKey(POLL, entity.id);
    let oldEntity = loadObj(key);
    Utils.assert(oldEntity === false, 'already exists');

    saveObj(key, entity);
    let poll_votes = [];
    saveObj(getKey(POLL_VOTES, entity.id), poll_votes);
    Chain.tlog('poll', 'the poll was created successfully', entity.id, entity.creator);
}

function revokePoll(inputObj){
    let id = inputObj.id;
    let key = getKey(POLL, id);
    let entity = loadObj(key);
    Utils.assert(entity !== false, 'not found');
    Utils.assert(entity.creator === Chain.msg.initiator, 'no permission');

    let poll_votes_key = getKey(POLL_VOTES, id);
    let vote_ids = loadObj(poll_votes_key);
    let idx = 0;
    for(idx =0; idx < vote_ids.length; idx+=1){
        let vid = vote_ids[idx];
        delObj(getKey(VOTE, vid));
    }
    delObj(key);
    delObj(poll_votes_key);
    Chain.tlog('poll', 'the poll was revoked', id, entity.creator);
}

function getPoll(id){
    let result = loadObj(getKey(POLL,id));
    return result;
}

function getVote(id){
    let key = getKey(VOTE, id);
    let result = loadObj(key);
    return result;
}

function closePoll(inputObj){
    let entity = inputObj.entity;
    let key = getKey(POLL, entity.id);

    let oldPoll = loadObj(key);
    Utils.assert(oldPoll !== false, 'not found');

    let owner = loadObj(OWNER);
    let caller = Chain.msg.initiator;
    Utils.assert(caller === owner, 'no permission');

    saveObj(key, entity);
    Chain.tlog('poll', 'the poll closed successfully', entity.id, entity.creator);
}


function vote(inputObj){
    let entity = inputObj.entity;
    let key = getKey(VOTE, entity.id);
    let oldEntity = loadObj(key);
    Utils.assert(oldEntity === false, 'already exists');

    let keyOfPoll = getKey(POLL, entity.pollId);
    let poll = loadObj(keyOfPoll);
    Utils.assert(poll !== false, 'not found');

    saveObj(key, entity);

    let poll_votes_key = getKey(POLL_VOTES, entity.pollId);
    let poll_votes = loadObj(poll_votes_key);
    poll_votes.push(entity.id);
    saveObj(poll_votes_key, poll_votes);

    Chain.tlog('poll', 'the vote was created successfully');
}


function query(input){
    let result={};
    let inputObj=JSON.parse(input);
    if(inputObj.method==='getPoll') {
        result=getPoll(inputObj.params.id);
    }else if(inputObj.method==='getVote'){
        result=getVote(inputObj.params.id);
    }else{
        result=false;
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
    let funcList= {'createPoll':createPoll,'revokePoll': revokePoll,'getPoll':getPoll,
     'vote':vote,'getVote':getVote, 'closePoll':closePoll};
    let inputObj=JSON.parse(input);
    Utils.assert(funcList.hasOwnProperty(inputObj.method) &&typeof funcList[inputObj.method]==='function','Cannot find func:'+inputObj.method);
    funcList[inputObj.method](inputObj.params);
}