# 📝 Conventional Commits Guide

## 🎯 Format de base

```
<type>(<scope>): <description>

[optional body]

[optional footer]
```

---

## 📦 Types de commit

| Type | Usage | Exemple |
|------|-------|---------|
| `feat` | Nouvelle fonctionnalité | `feat(session): add voting session activation` |
| `fix` | Correction de bug | `fix(api): resolve null pointer in controller` |
| `docs` | Documentation uniquement | `docs(readme): update installation steps` |
| `style` | Formatage (pas de logique) | `style(dto): apply spotless formatting` |
| `refactor` | Refactoring (ni feat ni fix) | `refactor(service): extract validation logic` |
| `test` | Ajout/modification de tests | `test(session): add unit tests for service` |
| `chore` | Tâches de maintenance | `chore(deps): upgrade Spring Boot to 3.5.3` |
| `perf` | Amélioration de performance | `perf(query): optimize database query` |
| `ci` | CI/CD | `ci(github): add pre-commit workflow` |
| `build` | Build system | `build(gradle): add spotless plugin` |
| `revert` | Annule un commit précédent | `revert: feat(session): add activation` |

---

## 📍 Scopes (contexte)

Pour VoteGuard :
- `session` - VotingSession
- `vote` - Vote features
- `user` - User management
- `api` - API endpoints
- `db` - Database
- `config` - Configuration
- `dto` - Data Transfer Objects
- `test` - Tests
- `deps` - Dependencies

---

## ✅ Exemples bons commits

```bash
# Feature
git commit -m "feat(session): implement soft delete for voting sessions"

# Bug fix
git commit -m "fix(controller): handle null values in CreateVotingSessionDTO"

# Documentation
git commit -m "docs(api): add Swagger documentation for session endpoints"

# Test
git commit -m "test(service): add edge cases for date validation"

# Refactoring
git commit -m "refactor(dto): extract validation to separate method"

# Style (après spotlessApply)
git commit -m "style: apply Google Java format to all files"

# Chore
git commit -m "chore(gradle): upgrade Lombok to 1.18.30"
```

---

## ❌ Exemples mauvais commits

```bash
# Trop vague
git commit -m "fix stuff"
git commit -m "update"
git commit -m "WIP"

# Pas de type
git commit -m "add new feature"

# Trop long
git commit -m "feat: add a new voting session creation endpoint that allows users to create sessions with validation and proper error handling and also includes comprehensive tests"

# Majuscule
git commit -m "Feat(session): Add feature"  # ❌ Minuscule obligatoire
```

---

## 💡 Body et Footer (optionnel)

### Avec body explicatif

```bash
git commit -m "feat(session): add pagination support

- Implement PageableConfig for global settings
- Add findByDeletedFalse method in repository
- Update controller to accept Pageable parameter
- Default page size: 10, max: 20"
```

### Avec référence issue

```bash
git commit -m "fix(api): resolve NPE in session creation

Fixes #42"
```

### Breaking change

```bash
git commit -m "feat(api)!: change session status enum values

BREAKING CHANGE: Status values changed from OPEN/CLOSED to ACTIVE/INACTIVE
Migration required for existing databases"
```

---

## 🔧 Configuration Git

### Créer un template de commit

```bash
# Créer le template
cat > ~/.gitmessage << 'EOF'
# <type>(<scope>): <description>
#
# Types: feat, fix, docs, style, refactor, test, chore, perf, ci, build, revert
# Scopes: session, vote, user, api, db, config, dto, test, deps
#
# Example: feat(session): add soft delete functionality
#
# Body (optional):
# - Explain WHY this change was made
# - Reference issues/tickets
#
# Footer (optional):
# Fixes #123
# BREAKING CHANGE: describe breaking changes
EOF

# Configurer Git pour l'utiliser
git config --global commit.template ~/.gitmessage
```

### Alias Git utiles

```bash
# Ajouter dans ~/.gitconfig

[alias]
    # Commit types
    feat = "!f() { git commit -m \"feat($1): $2\"; }; f"
    fix = "!f() { git commit -m \"fix($1): $2\"; }; f"
    docs = "!f() { git commit -m \"docs($1): $2\"; }; f"
    style = "!f() { git commit -m \"style($1): $2\"; }; f"
    
    # Quick commit
    qc = "!git add -A && git commit -m"
    
    # Amend last commit
    amend = commit --amend --no-edit
    
    # Show last commit
    last = log -1 HEAD --stat
```

**Usage :**
```bash
git feat session "add activation endpoint"
# → feat(session): add activation endpoint

git qc "style: apply spotless"
# → Quick commit avec le message
```

---

## 📊 Workflow recommandé

### 1. Développement classique

```bash
# Créer une branche feature
git checkout -b feat/session-activation

# Faire des commits atomiques
git add src/main/java/com/voteguar/app/votingsession/VotingSessionService.java
git commit -m "feat(session): add activation method"

git add src/test/java/com/voteguar/app/votingsession/VotingSessionServiceTest.java
git commit -m "test(session): add activation tests"

# Merger dans main
git checkout main
git merge feat/session-activation
```

### 2. Fix rapide

```bash
# Créer une branche fix
git checkout -b fix/null-pointer

# Fix + Test
git add .
git commit -m "fix(api): handle null in DTO validation"

# Merger
git checkout main
git merge fix/null-pointer
```

### 3. Refactoring

```bash
git checkout -b refactor/service-layer

# Plusieurs petits commits
git commit -m "refactor(service): extract validation logic"
git commit -m "refactor(service): simplify error handling"
git commit -m "test(service): update tests after refactoring"

git checkout main
git merge refactor/service-layer
```

---

## 🎓 Bonnes pratiques

### ✅ À FAIRE
1. **Commit atomique** : Un commit = Une modification logique
2. **Message clair** : Décrit QUOI a changé
3. **Body si nécessaire** : Explique POURQUOI
4. **Commit souvent** : Petits commits réguliers
5. **Tests avec la feature** : Commit séparé pour les tests

### ❌ À ÉVITER
1. ❌ Commits massifs avec 20 fichiers
2. ❌ Messages vagues ("update", "fix")
3. ❌ Mélanger plusieurs features
4. ❌ Commiter du code non testé
5. ❌ Oublier le type de commit

---

## 🔍 Vérifier l'historique

```bash
# Voir les derniers commits
git log --oneline -10

# Filtrer par type
git log --oneline --grep="^feat"

# Voir les stats
git shortlog -sn --no-merges

# Changelog automatique
git log --oneline --grep="^feat\|^fix" --since="1 month ago"
```

---

## 🤖 Intégration avec pre-commit

Tu peux ajouter une vérification du format dans le pre-commit :

```bash
# Ajouter dans .git/hooks/pre-commit

# Vérifier le format du message
COMMIT_MSG_FILE=$1
COMMIT_MSG=$(cat "$COMMIT_MSG_FILE")

PATTERN="^(feat|fix|docs|style|refactor|test|chore|perf|ci|build|revert)(\([a-z]+\))?: .+"

if ! echo "$COMMIT_MSG" | grep -qE "$PATTERN"; then
    echo "❌ Invalid commit message format"
    echo "Format: <type>(<scope>): <description>"
    exit 1
fi
```

---

## 📚 Ressources

- [Conventional Commits](https://www.conventionalcommits.org/)
- [Angular Commit Guidelines](https://github.com/angular/angular/blob/main/CONTRIBUTING.md#commit)
- [Semantic Versioning](https://semver.org/)

---

## 🎯 Objectif

Des commits bien formatés permettent :
- 📊 **Changelog automatique**
- 🔍 **Recherche facile dans l'historique**
- 🤝 **Collaboration efficace**
- 🚀 **CI/CD optimisé**
- 💼 **Portfolio professionnel**

**Parfait pour ton alternance Product Owner !** 🚀
