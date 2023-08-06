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

validateJiraTicket(jiraTicketRegex, branchName, 'Branch name');
validateJiraTicket(jiraTicketRegex, prTitle, 'PR title');

