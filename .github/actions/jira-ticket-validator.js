const core = require('@actions/core');

function validateJiraTicket(jiraTicketRegex, input, fieldName) {
    if (jiraTicketRegex.test(input)) {
        console.log(`${fieldName} is valid.`);
    } else {
        console.error(`${fieldName} is invalid. It should have a Jira ticket number in the format "HS-21".`);
        core.setFailed('Jira ticket number validation failed.');
    }
}

const jiraTicketRegex = /^HS-\d+(\s\|\s\w.*)?$/;

const branchName = process.env.GITHUB_HEAD_REF || '';
const prTitle = process.env.GITHUB_HEAD_REF || '';
const commitMessages = process.env.GITHUB_EVENT_PULL_REQUEST_COMMITS || '';

validateJiraTicket(jiraTicketRegex, branchName, 'Branch name');
validateJiraTicket(jiraTicketRegex, prTitle, 'PR title');

// Split the commit messages into an array
const commitMessageArray = commitMessages.split('\n');

commitMessageArray.forEach((message, index) => {
    validateJiraTicket(jiraTicketRegex, message, `Commit message ${index + 1}`);
});
