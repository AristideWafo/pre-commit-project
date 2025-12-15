# 🛡️ VoteGuard Pre-commit Configuration

## 📋 Vue d'ensemble

Ce projet utilise des **pre-commit hooks** pour garantir la qualité du code avant chaque commit.

### Vérifications effectuées (≈30 secondes)

1. ✅ **Formatage du code** (Google Java Style via Spotless)
2. ✅ **Compilation** (Java + Tests)
3. ⚠️ **System.out.println** (Warning uniquement)
4. 🔒 **Secrets** (Détection de credentials)
5. 🧪 **Tests unitaires** (Uniquement pour les fichiers modifiés)

---

## 🚀 Installation

### Étape 1 : Mettre à jour build.gradle

Remplace ton `build.gradle` actuel par la version fournie qui inclut Spotless.

**Changements apportés :**
```gradle
plugins {
    // ... autres plugins
    id 'com.diffplug.spotless' version '6.25.0'
}

// Configuration Spotless en bas du fichier
spotless {
    java {
        googleJavaFormat('1.19.2').aosp()
        removeUnusedImports()
        // ... autres règles
    }
}
```

### Étape 2 : Installer le hook pre-commit

```bash
# Se placer à la racine du projet
cd /path/to/VoteGuard

# Copier le hook
cp pre-commit .git/hooks/pre-commit

# Rendre le hook exécutable
chmod +x .git/hooks/pre-commit
```

### Étape 3 : Premier formatage

```bash
# Applique le formatage Google Java Style sur tout le code
./gradlew spotlessApply

# Vérifie que tout est OK
./gradlew spotlessCheck
```

### Étape 4 : Tester le hook

```bash
# Fait un commit de test
git add .
git commit -m "test: pre-commit setup"

# Tu devrais voir les 5 vérifications s'exécuter ✅
```

---

## 📖 Utilisation quotidienne

### Workflow normal

```bash
# 1. Modifier du code
vim src/main/java/com/voteguar/app/votingsession/VotingSession.java

# 2. Formatter automatiquement (optionnel)
./gradlew spotlessApply

# 3. Commiter normalement
git add .
git commit -m "feat: add new field to VotingSession"

# Le hook s'exécute automatiquement ✅
```

### Si une vérification échoue

#### ❌ Problème de formatage
```bash
# Message d'erreur
❌ Code formatting failed!
💡 Fix it automatically with: ./gradlew spotlessApply

# Solution
./gradlew spotlessApply
git add .
git commit -m "your message"
```

#### ❌ Tests en échec
```bash
# Message d'erreur
❌ Tests failed!
💡 Fix failing tests before committing

# Solution
# Corrige les tests
./gradlew test --tests "*VotingSessionTest"
git add .
git commit -m "your message"
```

#### ❌ Secrets détectés
```bash
# Message d'erreur
❌ Potential secrets detected!

# Solution 1 : Retirer le secret
# Déplacer les credentials vers application.properties (qui est ignoré)

# Solution 2 : Commit d'urgence (USE WITH CAUTION)
git commit --no-verify -m "your message"
```

---

## ⚡ Commandes utiles

### Formatter le code
```bash
# Appliquer le formatage sur tout le projet
./gradlew spotlessApply

# Vérifier sans modifier
./gradlew spotlessCheck

# Formatter un fichier spécifique
./gradlew :spotlessApply -PspotlessFiles=src/main/java/com/voteguar/app/votingsession/VotingSession.java
```

### Lancer les tests manuellement
```bash
# Tous les tests
./gradlew test

# Tests d'une classe spécifique
./gradlew test --tests "*VotingSessionTest"

# Tests avec verbose
./gradlew test --info
```

### Bypasser le pre-commit (URGENCE UNIQUEMENT)
```bash
# Skip toutes les vérifications
git commit --no-verify -m "hotfix: critical bug"

# Ou utiliser l'alias
git commit -n -m "hotfix: critical bug"

# ⚠️ À utiliser avec parcimonie !
```

---

## 🔧 Configuration avancée

### Modifier les règles Spotless

Édite `build.gradle` :

```gradle
spotless {
    java {
        // Exemple : Changer la longueur max des lignes
        googleJavaFormat('1.19.2').aosp().reflowLongStrings()
        
        // Exemple : Ajouter une licence header
        licenseHeaderFile 'config/license-header.txt'
    }
}
```

### Ajouter des vérifications

Édite `.git/hooks/pre-commit` pour ajouter tes propres checks :

```bash
# Exemple : Vérifier TODO dans le code
echo "Checking for TODO comments..."
TODO_COUNT=$(git diff --cached | grep -c "TODO" || true)
if [ $TODO_COUNT -gt 5 ]; then
    echo "⚠️  Too many TODOs ($TODO_COUNT)"
fi
```

---

## 🐛 Troubleshooting

### Le hook ne s'exécute pas
```bash
# Vérifier les permissions
ls -la .git/hooks/pre-commit

# Si nécessaire
chmod +x .git/hooks/pre-commit
```

### Spotless échoue sur les anciens fichiers
```bash
# Solution 1 : Formatter tout le projet
./gradlew spotlessApply

# Solution 2 : Formatter progressivement
git diff --name-only | xargs ./gradlew spotlessApply -PspotlessFiles=
```

### Les tests sont trop lents
```bash
# Éditer .git/hooks/pre-commit
# Ligne ~100, commenter la section tests :

# if [ -z "$MODIFIED_JAVA_FILES" ]; then
#     echo "No tests to run"
# fi
```

### Erreur "gradlew: command not found"
```bash
# Vérifier que tu es à la racine du projet
pwd  # Doit afficher /path/to/VoteGuard

# Vérifier que gradlew existe
ls -la gradlew
```

---

## 📊 Statistiques de performance

| Vérification | Temps moyen |
|--------------|-------------|
| Spotless     | ~5s         |
| Compilation  | ~10s        |
| System.out   | <1s         |
| Secrets      | <1s         |
| Tests        | ~15s        |
| **TOTAL**    | **~30s**    |

---

## 🎓 Bonnes pratiques

### ✅ À FAIRE
- Commiter souvent avec des petits changements
- Lancer `./gradlew spotlessApply` avant de commiter
- Écrire des tests pour chaque nouvelle fonctionnalité
- Communiquer avec l'équipe si tu dois bypasser

### ❌ À ÉVITER
- Commiter du code non compilé
- Abuser de `--no-verify`
- Commiter des System.out.println
- Ignorer les warnings

---

## 📚 Ressources

- [Spotless Documentation](https://github.com/diffplug/spotless)
- [Google Java Style Guide](https://google.github.io/styleguide/javaguide.html)
- [Git Hooks Documentation](https://git-scm.com/book/en/v2/Customizing-Git-Git-Hooks)

---

## 🤝 Contribution

Si tu veux améliorer les pre-commits :

1. Modifier `.git/hooks/pre-commit`
2. Tester sur un commit de test
3. Partager avec l'équipe

---

## 📞 Support

En cas de problème :
1. Vérifie ce README
2. Lance `./gradlew clean build` pour reset
3. Contacte l'équipe dev

**Dernière mise à jour : Décembre 2025**
