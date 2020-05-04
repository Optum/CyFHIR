# Contributing to *CyFHIR*

## Introduction

Thank you for considering a contribution to *CyFHIR*. Your support of this project within Optum OpenSource directly contributes to the mission of Optum and UHG to help people live healthier lives and to help make the health system work better for everyone.

Please note that this project is released with a [Contributor Code of Conduct](CODE_OF_CONDUCT.md). By participating in this project you agree to abide by its terms. Please also review our [Individual Contributor License Agreement ("ICL")](INDIVIDUAL_CONTRIBUTOR_LICENSE.md) prior to submitting changes to the project.  You will need to attest to this agreement following the instructions in the [Paperwork for Pull Requests](#paperwork-for-pull-requests) section below.


## Code of Conduct

This project and everyone participating in it is governed by the [Contributor Code of Conduct](CODE_OF_CONDUCT.md). By participating, you are expected to uphold this code. Please report unacceptable behavior to [matthew.frisby@optum.com][email].

## How to Contribute

* We are open to all kinds of contributions right now, so if you find an issue, bug, or have a fresh idea for CyFHIR consider reaching out or adding to our project kanban board
* Create an issue or open a PR before you begin working on anything and add the *WIP* label as well as any other label that matches your issue or PR
* When you are ready to be reviewed, remove the *WIP* label from your issue or PR and we will review it

## Paperwork for Pull Requests

* Please read this guide and make sure you agree with our [Individual Contributor License Agreement ("ICL")](INDIVIDUAL_CONTRIBUTOR_LICENSE.md).
* Make sure git knows your name and email address:
   ```
   $ git config user.name "J. Random User"
   $ git config user.email "j.random.user@example.com"
   ```
> Signing-Off on your commit is agreeing with our ICL and attesting that you either wrote or have the rights to contribute the code. The name and email address must be valid as we cannot accept anonymous contributions.
* Write good commit messages
* Sign-off every commit `git commit --signoff` or `git commit -s`, as directed by the ICL.

> If you forget to sign a commit, then you’ll have to do a bit of rewriting history. Don’t worry. It’s pretty easy. If it’s the latest commit, you can just run either `git commit -a -s` or `git commit --amend --signoff` to fix things. It gets a little trickier if you have to change something farther back in history but there are some good instructions [here](https://git-scm.com/book/en/v2/Git-Tools-Rewriting-History) in the Changing Multiple Commit Messages section.

## Style Guides

For Javascript we use es-linter and our style preference is will be in .eslintrc.json files

For everything else, we aren't too picky as long as the code is clean and organized.

### Git Commit Messages

* Use the present tense ("Add feature" not "Added feature")
* Use the imperative mood ("Move cursor to..." not "Moves cursor to...")
* Limit the first line to 72 characters or less
* Reference issues and pull requests liberally after the first line

[email]: mailto:matthew.frisby@optum.com
