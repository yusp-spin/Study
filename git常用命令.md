---
title: Git常用命令
date: 2020-11-12 21:58:13
categories: Git
---

git常用操作总结一下，也相当于做个笔记

##### 1、git 配置


git config user.name  查看 用户名

git config user.email   查看 邮箱

git config --global user.name <name>  修改 用户名

git config --global user.email <email>  修改 邮箱

ssh-keygen -t rsa -C " your_email@example.com" 创建SSH key 【可以填写任意值作为注释key，例如邮箱】

ssh -T git@gitee.com 测试该SHH key 已添加到 gitee.com【码云】

#####  2、创建版本库


git init  初始化本地版本库【创建一个 .git的子目录】

git init [project-name]  新建一个目录，并将其初始化为 git 代码库；

git clone <url>  克隆远程版本库；

#####  3、修改和提交


git status  显示文件的状态 【红色表示被修改没提交到暂存区，绿色代表已提交到暂存区；】

git status -s  以极简的方式显示文件的状态【红色的M 表示被修改没提交到暂存区，绿色的M代表已提交到暂存区；】

 

**git add**  将文件从工作目录添加至暂存区；

　　git add -u | --update  仅将被修改的文件添加至暂存区（不包含新添加的文件）；

　　git add .  将被修改的文件 和 新添加的文件提交到暂存区（不包含已经删除的文件）；

　　git add -a  将本地所有修改的内容添加至暂存区（包含新添加的 和 已经删除的）；

 

**git commit**  将暂存区的修改提交到本地仓库，同时生成一个commit-id；

　　git commit -m <message>  将暂存区修改提交到本地仓库

　　git commit -a -m <message>  将工作区的修改提交到本地仓库 【相当于 **git add + git commit** 】

　　git commit -amend  修改上一次提交【代码没有任何变化，则修改提交信息】

 

##### 4、分支操作

**git branch**

　　git branch  列出所有本地分支

　　git branch -r  列出所有远程分支

　　git branch -a 列出所有本地和远程分支

　　git branch [branch-name]  新建一个分支，仍停留在当前分支

　　git branch -m <nameA> <nameB>  将分支nameA 改名为 nameB

　　git branch -d [branch-name] 删除分支

 

**git checkout**

　　git checkout [branch-name]  切换到指定分支

　　git checkout -b [branch-name]  新建一个分支，并切换到该分支

　　git checkout - 切换到上一个分支

 

**git merge**

　　git merge [branch-name] 合并指定分支到当前分支

#####  5、远程操作


git fetch 将远程主机上所有分支的更新取回本地，并记录在 .git/FETCH_HEAD 中；

　　git fetch <remote-name> 下载远程仓库的所有变动；

　　git fetch <remote-name> master:test 在本地新建test 分支，并将主机上master分支代码下载到本地 test 分支；


**git remote**

　　git remote -v  显示所有远程仓库

　　git remote show <remote-name> 显示某个远程仓库的信息

　　git remote add <remote-name> [ url ] 增加一个新的远程仓库 并命名


**git pull**

　　git pull <远程主机名> <远程分支名> : <本地分支名>  取回远程仓库某个分支的更新，并与本地分支合并

　　git pull origin dev: master 取回远程主机的 dev 分支，与本地的master分支合并

　　git pull origin dev 相当于以下两个命令：

　　　　git fetch origin 获取远程主机上所有分支的更新

　　　　git merge origin/dev 与当前分支合并


**git push**

　　git push <远程主机名> <本地分支名> : <远程分支> 上传本地指定分支到远程仓库的指定分支

　　　　省略远程分支名，表示将本地分支推送到与之存在“追踪关系”的远程分支，通常两者同名，后者不存在，将会被创建；

　　　　省略本地分支名，表示删除指定的远程分支【这相当推送一个空的本地分支到远程分支】；

　　git push origin master 将本地的master 分支推送到 origin 主机的master 分支【后者不存在，将会被创建】

　　git push origin : master 删除 origin 主机的master 分支；【相当于 git push origin --delete master】

#####  6、撤销修改


撤销工作区的修改： 【文件修改之后撤销】

　　git checkout -- file 恢复暂存区的指定文件到工作区

　　git checkout . 恢复暂存区的所有文件到工作区

 

撤销暂存区的修改： 【git add 之后】

　　git reste HEAD <file>

 

版本回退 ：

　　 git reset --hard <commit_id> 


git log 查看提交历史，确定回退到那个版本；

git reflog 查看历史命令，确定回到未来的版本；

 



参考：https://www.cnblogs.com/james23dong/p/12375970.html