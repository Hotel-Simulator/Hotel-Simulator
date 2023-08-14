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

const eventData = JSON.parse(fs.readFileSync(process.env.GITHUB_EVENT_PATH, 'utf8'));

const branchName = eventData.pull_request.head.ref || '';
const prTitle = eventData.pull_request.title || '';

console.log(`Validating branch name: ${branchName}`);
validateJiraTicket(jiraTicketRegex, branchName, 'Branch name');
console.log(`Validating PR title: ${prTitle}`);
validateJiraTicket(jiraTicketRegex, prTitle, 'PR title');

