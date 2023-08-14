const axios = require('axios');
const core = require('@actions/core');

function validateJiraTicket(jiraTicketRegex, input, fieldName) {
    if (jiraTicketRegex.test(input)) {
        console.log(`${fieldName} is valid.`);
    } else {
        console.error(`${fieldName} is invalid. It should have a Jira ticket number in the format "HS-21". (${input})`);
        core.setFailed('Jira ticket number validation failed.');
    }
}

const jiraTicketRegex = /^HS-\d+(\s\|\s\w.*)?$/;

const branchName = process.env.GITHUB_HEAD_REF || '';
const prNumber = process.env.GITHUB_EVENT_NUMBER;

const response = await axios.get(
    `https://api.github.com/repos/${process.env.GITHUB_REPOSITORY}/pulls/${prNumber}`,
    {
        headers: {
            Authorization: `Bearer ${process.env.GITHUB_TOKEN}`,
        },
    }
);

const prTitle = response.data.title;

console.log(`Validating branch name: ${branchName}`);
validateJiraTicket(jiraTicketRegex, branchName, 'Branch name');
console.log(`Validating PR title: ${prTitle}`);
validateJiraTicket(jiraTicketRegex, prTitle, 'PR title');

