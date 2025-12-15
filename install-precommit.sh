#!/bin/bash

# ============================================
# Script d'installation des pre-commits VoteGuard
# ============================================

set -e

BLUE='\033[0;34m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
RED='\033[0;31m'
NC='\033[0m'

echo -e "${BLUE}════════════════════════════════════════${NC}"
echo -e "${BLUE}  VoteGuard Pre-commit Installation${NC}"
echo -e "${BLUE}════════════════════════════════════════${NC}"
echo ""

# Vérifier qu'on est à la racine du projet
if [ ! -f "build.gradle" ]; then
    echo -e "${RED}❌ Erreur: build.gradle non trouvé${NC}"
    echo -e "${YELLOW}💡 Assure-toi d'être à la racine du projet VoteGuard${NC}"
    exit 1
fi

# Vérifier que gradlew existe
if [ ! -f "gradlew" ]; then
    echo -e "${RED}❌ Erreur: gradlew non trouvé${NC}"
    exit 1
fi

echo -e "${BLUE}[1/4]${NC} Checking prerequisites..."
echo -e "${GREEN}✅ Project structure OK${NC}"
echo ""

# Backup du build.gradle actuel
echo -e "${BLUE}[2/4]${NC} Backing up current build.gradle..."
if [ -f "build.gradle" ]; then
    cp build.gradle build.gradle.backup
    echo -e "${GREEN}✅ Backup created: build.gradle.backup${NC}"
fi
echo ""

# Copier le nouveau build.gradle avec Spotless
echo -e "${BLUE}[3/4]${NC} Updating build.gradle with Spotless configuration..."
cat > build.gradle << 'EOF'
plugins {
	id 'java'
	id 'org.springframework.boot' version '3.5.3'
	id 'io.spring.dependency-management' version '1.1.7'
	id 'com.diffplug.spotless' version '6.25.0'
}

group = 'com.voteguar'
version = '0.0.1-SNAPSHOT'

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(24)
	}
}

configurations {
	compileOnly {
		extendsFrom annotationProcessor
	}
}

repositories {
	mavenCentral()
}

dependencies {
	implementation 'org.springframework.boot:spring-boot-starter-data-jpa'
	implementation 'org.springframework.boot:spring-boot-starter-thymeleaf'
	implementation 'org.springframework.boot:spring-boot-starter-web'
	compileOnly 'org.projectlombok:lombok'
	developmentOnly 'org.springframework.boot:spring-boot-devtools'
	runtimeOnly 'com.mysql:mysql-connector-j'
	annotationProcessor 'org.projectlombok:lombok'
	testImplementation 'org.springframework.boot:spring-boot-starter-test'
	testRuntimeOnly 'org.junit.platform:junit-platform-launcher'
}

tasks.named('test') {
	useJUnitPlatform()
}

// ========================================
// Configuration Spotless (Google Java Style)
// ========================================
spotless {
	java {
		googleJavaFormat('1.19.2').aosp().reflowLongStrings()
		removeUnusedImports()
		importOrder('java', 'javax', '', '\\#')
		trimTrailingWhitespace()
		endWithNewline()
		target 'src/**/*.java'
		targetExclude 'build/**', 'bin/**'
	}
}
EOF
echo -e "${GREEN}✅ build.gradle updated${NC}"
echo ""

# Installer le hook pre-commit
echo -e "${BLUE}[4/4]${NC} Installing pre-commit hook..."
mkdir -p .git/hooks

# Créer le pre-commit hook
cat > .git/hooks/pre-commit << 'HOOK_EOF'
#!/bin/bash

set -e

echo "🔍 Starting pre-commit checks..."
echo ""

RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m'

# 1. Spotless Check
echo -e "${BLUE}[1/5]${NC} Checking code formatting (Spotless)..."
if ! ./gradlew spotlessCheck --quiet; then
    echo -e "${RED}❌ Code formatting failed!${NC}"
    echo -e "${YELLOW}💡 Fix it automatically with:${NC} ./gradlew spotlessApply"
    exit 1
fi
echo -e "${GREEN}✅ Code formatting OK${NC}"
echo ""

# 2. Compilation
echo -e "${BLUE}[2/5]${NC} Compiling Java sources..."
if ! ./gradlew compileJava compileTestJava --quiet; then
    echo -e "${RED}❌ Compilation failed!${NC}"
    exit 1
fi
echo -e "${GREEN}✅ Compilation OK${NC}"
echo ""

# 3. System.out.println detection
echo -e "${BLUE}[3/5]${NC} Checking for System.out.println..."
SYSOUT_FILES=$(git diff --cached --name-only --diff-filter=ACM | grep "\.java$" | xargs grep -l "System\.out\.print" 2>/dev/null || true)

if [ -n "$SYSOUT_FILES" ]; then
    echo -e "${YELLOW}⚠️  Warning: System.out.println detected in:${NC}"
    echo "$SYSOUT_FILES" | while read -r file; do
        LINE_NUMBERS=$(grep -n "System\.out\.print" "$file" | cut -d: -f1 | paste -sd "," -)
        echo -e "   ${YELLOW}→${NC} $file:$LINE_NUMBERS"
    done
    echo -e "${YELLOW}💡 Consider using a logger (e.g., @Slf4j) instead${NC}"
    echo ""
else
    echo -e "${GREEN}✅ No System.out.println found${NC}"
    echo ""
fi

# 4. Secret detection
echo -e "${BLUE}[4/5]${NC} Checking for potential secrets..."
SECRET_PATTERNS="password=|apikey=|api_key=|secret=|token=|jdbc.*password"
SECRETS_FOUND=$(git diff --cached | grep -iE "$SECRET_PATTERNS" | grep -v "application.properties" | grep -v "application.yml" || true)

if [ -n "$SECRETS_FOUND" ]; then
    echo -e "${RED}❌ Potential secrets detected!${NC}"
    echo "$SECRETS_FOUND"
    exit 1
fi
echo -e "${GREEN}✅ No secrets detected${NC}"
echo ""

# 5. Tests (fichiers modifiés uniquement)
echo -e "${BLUE}[5/5]${NC} Running tests for modified files..."
MODIFIED_JAVA_FILES=$(git diff --cached --name-only --diff-filter=ACM | grep "src/main/java/.*\.java$" || true)

if [ -z "$MODIFIED_JAVA_FILES" ]; then
    echo -e "${GREEN}✅ No Java source files modified${NC}"
else
    TEST_CLASSES=""
    for file in $MODIFIED_JAVA_FILES; do
        CLASS_NAME=$(basename "$file" .java)
        POSSIBLE_TESTS="*${CLASS_NAME}*Test"
        
        if find src/test/java -name "*${CLASS_NAME}*Test.java" 2>/dev/null | grep -q .; then
            TEST_CLASSES="$TEST_CLASSES $POSSIBLE_TESTS"
            echo -e "   ${BLUE}→${NC} Will test: $POSSIBLE_TESTS"
        fi
    done
    
    if [ -z "$TEST_CLASSES" ]; then
        echo -e "${YELLOW}⚠️  No tests found for modified files${NC}"
    else
        echo ""
        if ! ./gradlew test --tests "$TEST_CLASSES" --quiet; then
            echo -e "${RED}❌ Tests failed!${NC}"
            exit 1
        fi
        echo -e "${GREEN}✅ All tests passed${NC}"
    fi
fi

echo ""
echo -e "${GREEN}━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━${NC}"
echo -e "${GREEN}✅ All pre-commit checks passed!${NC}"
echo -e "${GREEN}━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━${NC}"
echo ""

exit 0
HOOK_EOF

chmod +x .git/hooks/pre-commit
echo -e "${GREEN}✅ Pre-commit hook installed${NC}"
echo ""

# Premier formatage du code
echo -e "${YELLOW}Applying Google Java Style to existing code...${NC}"
if ./gradlew spotlessApply --quiet; then
    echo -e "${GREEN}✅ Code formatted successfully${NC}"
else
    echo -e "${YELLOW}⚠️  Some files may need manual review${NC}"
fi
echo ""

# Résumé
echo -e "${GREEN}════════════════════════════════════════${NC}"
echo -e "${GREEN}  ✅ Installation completed!${NC}"
echo -e "${GREEN}════════════════════════════════════════${NC}"
echo ""
echo -e "${BLUE}Next steps:${NC}"
echo -e "  1. Review formatted files: ${YELLOW}git status${NC}"
echo -e "  2. Test the hook: ${YELLOW}git commit -m 'test'${NC}"
echo -e "  3. Read the guide: ${YELLOW}cat PRECOMMIT-GUIDE.md${NC}"
echo ""
echo -e "${BLUE}Quick commands:${NC}"
echo -e "  Format code:  ${YELLOW}./gradlew spotlessApply${NC}"
echo -e "  Check format: ${YELLOW}./gradlew spotlessCheck${NC}"
echo -e "  Run tests:    ${YELLOW}./gradlew test${NC}"
echo -e "  Skip hook:    ${YELLOW}git commit --no-verify${NC}"
echo ""
echo -e "${GREEN}Happy coding! 🚀${NC}"
