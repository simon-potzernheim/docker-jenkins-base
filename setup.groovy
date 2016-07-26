import jenkins.model.*
import jenkins.security.ApiTokenProperty
import jenkins.branch.BranchProjectFactory;
import jenkins.branch.MultiBranchProject
import jenkins.branch.OrganizationFolder
import jenkins.install.*
import hudson.security.*
import hudson.model.User
import hudson.plugins.git.GitSCM;
import hudson.plugins.sonar.*
import hudson.plugins.sonar.model.*
import hudson.tools.*
//@GrabResolver(name='jenkinsrepo', root='http://repo.jenkins-ci.org/releases/')
//@Grapes(@Grab(group='org.jenkins-ci.plugins', module='credentials', version='2.0.1'))
import com.cloudbees.plugins.credentials.impl.*;
import com.cloudbees.plugins.credentials.*;
import com.cloudbees.plugins.credentials.domains.*;
import org.jenkinsci.plugins.github_branch_source.Endpoint;
import org.jenkinsci.plugins.github_branch_source.GitHubConfiguration
import org.jenkinsci.plugins.github_branch_source.GitHubSCMNavigator
import org.jenkinsci.plugins.orgfolder.github.GitHubOrgAction
import org.jenkinsci.plugins.orgfolder.github.GitHubRepoAction
import org.kohsuke.github.GHOrganization;
import org.kohsuke.github.GHRepository
import org.kohsuke.github.GHUser
import org.kohsuke.github.GitHub
import org.kohsuke.github.GitHubBuilder;
import org.jenkinsci.plugins.workflow.multibranch.WorkflowMultiBranchProject

//println "--> set number of executors to 5"
//
//Jenkins.instance.setNumExecutors(5)
//
//println "--> creating local user 'Simon'"

def hudsonRealm = new HudsonPrivateSecurityRealm(true)
hudsonRealm.createAccount('simon','foobar')
Jenkins.instance.setSecurityRealm(hudsonRealm)

def strategy = new FullControlOnceLoggedInAuthorizationStrategy()
Jenkins.instance.setAuthorizationStrategy(strategy)
Jenkins.instance.save()

println "--> configure maven"
println "Adding an auto installer for Maven 3.3.9"

def mavenPluginExtension = Jenkins.instance.getExtensionList(hudson.tasks.Maven.DescriptorImpl.class)[0]
def asList = (mavenPluginExtension.installations as List)
asList.add(new hudson.tasks.Maven.MavenInstallation('maven-3', null, [
    new hudson.tools.InstallSourceProperty([
        new hudson.tasks.Maven.MavenInstaller("3.3.9")
    ])
]))
mavenPluginExtension.installations = asList
mavenPluginExtension.save()

println "OK - Maven auto-installer (from Apache) added for 3.3.9"
