const resetVariables = () => {
    bru.setEnvVar();
};

const setVariables = () => {
};

const globalSettings = () => {
};

const generateRandomString = (length) => {
    let result = '';
    const characters = 'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789';
    for (let i = 0; i < length; i++) {
        result += characters.charAt(Math.floor(Math.random() * characters.length));
    }
    return result;
};

const generateRandomUser = () => {
    const randomStr = generateRandomString(8);
    const userName = `user_${randomStr}`;
    const email = `${userName}@example.com`;
    bru.setVar("testUserName", userName);
    bru.setVar("testEmail", email);
    bru.setVar("testPassword", "password123");
};

module.exports = {
    resetVariables,
    setVariables,
    globalSettings,
    generateRandomUser,
    generateRandomString
};