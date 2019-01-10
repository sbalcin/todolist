import * as types from '../actions/actionTypes';
import axios from 'axios';
import {signUpUri, signInUri} from "../config/constants";


export const authSignUp = (email, password, name) => {
    var data = {
        nameSurname: name,
        email: email,
        password: password
    };

    return axios.post(signUpUri, data).then(response => {
        if (response.data.result == 'success') {
            return {user: data, error: null};
        } else {
            return {user: null, error: response.data.result};
        }
    }).catch(err => {
        return {user: null, error: err.toString()};
    })
}

export const authSignIn = (email, password) => {
    var data = {
        email: email,
        password: password
    };

    return axios.post(signInUri, data).then(response => {
        if (response.data.result == 'success') {
            return {user: response.data.token, error: null};
        } else {
            return {user: null, error: response.data.result};
        }
    }).catch(err => {
        return {user: null, error: err.toString()};
    })
}

export const authSignOut = () => {
    return {isSignedOut: true};
}
