# 📦 VoteGuard Pre-commit Package

Configuration complète des pre-commit hooks pour le projet VoteGuard.

## 📁 Fichiers inclus

```
voteguard-precommit/
├── build.gradle                  # Configuration Gradle avec Spotless
├── pre-commit                    # Hook Git à installer
├── install-precommit.sh          # Script d'installation automatique
└── PRECOMMIT-GUIDE.md           # Guide complet d'utilisation
```

---

## 🚀 Installation Rapide (Recommandée)

### Option 1 : Installation automatique

```bash
# 1. Copie tous les fichiers à la racine de VoteGuard
cp -r voteguard-precommit/* /path/to/VoteGuard/

# 2. Lance le script d'installation
cd /path/to/VoteGuard
chmod +x install-precommit.sh
./install-precommit.sh

# 3. C'est tout ! ✅
```

Le script va :
- ✅ Backup ton build.gradle actuel
- ✅ Installer Spotless
- ✅ Créer le hook pre-commit
- ✅ Formatter tout le code existant

---

## 🔧 Installation Manuelle

### Étape 1 : Backup et mise à jour du build.gradle

```bash
cd /path/to/VoteGuard

# Backup
cp build.gradle build.gradle.backup

# Copie le nouveau build.gradle
cp voteguard-precommit/build.gradle build.gradle
```

### Étape 2 : Installation du hook

```bash
# Copie le hook
cp voteguard-precommit/pre-commit .git/hooks/pre-commit

# Rend le hook exécutable
chmod +x .git/hooks/pre-commit
```

### Étape 3 : Premier formatage

```bash
# Applique Google Java Style sur tout le code
./gradlew spotlessApply

# Vérifie que tout est OK
./gradlew spotlessCheck
```

---

## ✅ Vérification de l'installation

```bash
# Test rapide
git add .
git commit -m "test: pre-commit installation"

# Tu devrais voir :
# 🔍 Starting pre-commit checks...
# [1/5] Checking code formatting (Spotless)...
# [2/5] Compiling Java sources...
# [3/5] Checking for System.out.println...
# [4/5] Checking for potential secrets...
# [5/5] Running tests for modified files...
# ✅ All pre-commit checks passed!
```

---

## 📋 Vérifications effectuées

| # | Vérification | Action | Temps |
|---|--------------|--------|-------|
| 1 | **Spotless** | Formatage Google Java Style | ~5s |
| 2 | **Compilation** | Compile Java + Tests | ~10s |
| 3 | **System.out** | Détecte les println (warning) | <1s |
| 4 | **Secrets** | Détecte credentials | <1s |
| 5 | **Tests** | Lance tests des fichiers modifiés | ~15s |

**Total : ~30 secondes**

---

## 🎯 Configuration choisie

### Google Java Style
- ✅ Format standard de Google
- ✅ Utilisé par des milliers de projets
- ✅ Compatible avec IntelliJ IDEA
- ✅ Style professionnel et cohérent

### Tests intelligents
- ✅ Lance uniquement les tests des fichiers modifiés
- ✅ Rapide : pas besoin de lancer toute la suite
- ✅ Fiable : détecte les régressions immédiates

### Bypass autorisé
- ✅ `git commit --no-verify` pour les urgences
- ⚠️ À utiliser avec parcimonie

---

## 📖 Documentation

Consulte `PRECOMMIT-GUIDE.md` pour :
- 📚 Guide d'utilisation détaillé
- 🔧 Configuration avancée
- 🐛 Troubleshooting
- 💡 Bonnes pratiques
- ⚡ Commandes utiles

---

## 🆘 En cas de problème

### Le hook ne fonctionne pas
```bash
# Vérifie les permissions
ls -la .git/hooks/pre-commit

# Rendre exécutable
chmod +x .git/hooks/pre-commit
```

### Spotless échoue
```bash
# Applique le formatage automatiquement
./gradlew spotlessApply

# Puis commit
git add .
git commit -m "style: apply Google Java format"
```

### Tests trop lents
```bash
# Ouvre .git/hooks/pre-commit
# Commente la section tests (ligne ~100)
```

---

## 💡 Commandes utiles

```bash
# Formatter le code
./gradlew spotlessApply

# Vérifier le formatage
./gradlew spotlessCheck

# Lancer tous les tests
./gradlew test

# Bypasser le hook (urgence)
git commit --no-verify -m "hotfix"

# Voir le statut Git
git status
```

---

## 🔄 Mise à jour

Pour mettre à jour les pre-commits :

```bash
# Re-lance simplement l'installation
./install-precommit.sh

# Ou modifie manuellement
vim .git/hooks/pre-commit
```

---

## 🤝 Support

Pour toute question :
1. Consulte `PRECOMMIT-GUIDE.md`
2. Lance `./gradlew clean build`
3. Vérifie les logs : `git --no-pager log -1`

---

## 📊 Impact attendu

### Avant pre-commits
- ❌ Styles de code incohérents
- ❌ Commits avec code non compilé
- ❌ System.out.println partout
- ❌ Tests cassés en prod

### Après pre-commits
- ✅ Code uniformément formatté
- ✅ Compilation garantie
- ✅ Code propre
- ✅ Tests validés avant merge

---

## 🎓 Contexte EPITECH

Cette configuration est optimisée pour :
- ✅ Projets académiques et professionnels
- ✅ Collaboration en équipe
- ✅ Préparation aux standards entreprise
- ✅ Portfolio GitHub professionnel

**Idéal pour ta recherche d'alternance Product Owner !** 🚀

---

**Version:** 1.0  
**Date:** Décembre 2025  
**Auteur:** Jerry Wafo Kamgue  
**Projet:** VoteGuard
