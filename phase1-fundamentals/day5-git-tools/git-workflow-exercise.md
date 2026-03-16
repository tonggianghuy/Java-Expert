# BÀI TẬP THỰC HÀNH GIT WORKFLOW

## Bài 1: Khởi tạo và commit cơ bản

```bash
# Bước 1: Tạo project
mkdir banking-exercises && cd banking-exercises
git init

# Bước 2: Tạo file đầu tiên
echo "# Banking Exercises" > README.md

# Bước 3: Kiểm tra trạng thái
git status
# → README.md sẽ hiện là "Untracked"

# Bước 4: Stage và commit
git add README.md
git commit -m "docs: add README.md"

# Bước 5: Xem lịch sử
git log --oneline
```

## Bài 2: Branching & Merging

```bash
# Bước 1: Tạo branch feature
git checkout -b feature/add-calculator

# Bước 2: Tạo file Calculator.java (copy từ Day 1)
# ... viết code ...

# Bước 3: Commit trên branch feature
git add Calculator.java
git commit -m "feat: add simple interest calculator"

# Bước 4: Tạo thêm 1 commit
# ... sửa code ...
git add .
git commit -m "feat: add currency converter function"

# Bước 5: Xem branches
git branch
# * feature/add-calculator
#   main

# Bước 6: Merge vào main
git checkout main
git merge feature/add-calculator

# Bước 7: Xem log dạng graph
git log --oneline --graph --all

# Bước 8: Xóa branch đã merge
git branch -d feature/add-calculator
```

## Bài 3: Giải quyết Conflict

```bash
# Bước 1: Tạo 2 branches từ main
git checkout -b feature/fee-v1
# Sửa file: double fee = amount * 0.001;
git add . && git commit -m "feat: add transfer fee 0.1%"

git checkout main
git checkout -b feature/fee-v2
# Sửa CÙNG DÒNG: double fee = amount * 0.002;
git add . && git commit -m "feat: add transfer fee 0.2%"

# Bước 2: Merge sẽ gây conflict
git checkout main
git merge feature/fee-v1    # OK
git merge feature/fee-v2    # CONFLICT!

# Bước 3: Mở file, giải quyết conflict
# Xóa markers <<<<<<< ======= >>>>>>>
# Chọn giá trị phù hợp

# Bước 4: Complete merge
git add .
git commit -m "merge: resolve fee calculation conflict, use 0.15%"
```

## Bài 4: .gitignore

Tạo file `.gitignore` với nội dung phù hợp cho project Java + TypeScript:

```bash
# Tạo .gitignore
cat > .gitignore << 'GITIGNORE'
# Java
*.class
*.jar
target/
.idea/
*.iml

# TypeScript / Node
node_modules/
dist/
*.js.map

# Environment
.env
.env.local

# OS
.DS_Store
Thumbs.db

# IDE
.vscode/settings.json
GITIGNORE

git add .gitignore
git commit -m "chore: add .gitignore for Java and TypeScript"
```

## Bài 5: Tổ chức lại toàn bộ code ngày 1-4

```bash
# Tạo cấu trúc folder
mkdir -p src/{day1,day2,day3,day4}

# Copy/move files vào đúng folder
# mv ATMConsole.java src/day1/
# mv SimpleInterestCalculator.java src/day1/
# mv ShoppingCart.java src/day2/
# mv BankAccount.java src/day3/
# mv product-catalog.ts src/day4/

# Commit từng phần
git add src/day1/
git commit -m "feat: organize day 1 exercises - Java basics"

git add src/day2/
git commit -m "feat: organize day 2 exercises - collections"

git add src/day3/
git commit -m "feat: organize day 3 exercises - OOP"

git add src/day4/
git commit -m "feat: organize day 4 exercises - TypeScript"
```

## Kiểm tra hoàn thành

```bash
# Log phải hiện ít nhất 5+ commits với messages chuẩn
git log --oneline

# Ví dụ output:
# a1b2c3d feat: organize day 4 exercises - TypeScript
# d4e5f6g feat: organize day 3 exercises - OOP
# h7i8j9k feat: organize day 2 exercises - collections
# l0m1n2o feat: organize day 1 exercises - Java basics
# p3q4r5s chore: add .gitignore for Java and TypeScript
# t6u7v8w docs: add README.md
```
