<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html lang="fr" class="h-full bg-white">
<head>
    <meta charset="UTF-8">
    <title>pisciculture - Accueil</title>
    <script src="https://cdn.tailwindcss.com"></script>
</head>
<body class="h-full overflow-hidden flex">
    <jsp:include page="/WEB-INF/jsp/components/sidebar.jsp" />

    <div class="flex-1 flex flex-col min-w-0 overflow-hidden">
        <header class="bg-white border-b border-gray-200">
            <div class="px-4 sm:px-6 lg:px-8">
                <div class="flex justify-between h-16 items-center">
                    <h1 class="text-lg font-semibold text-gray-900">Tableau de bord</h1>
                </div>
            </div>
        </header>

        <main class="flex-1 overflow-y-auto bg-gray-50 p-6">
            <div class="max-w-7xl mx-auto">
                <!-- Welcome Section -->
                <div class="bg-white rounded-lg shadow-sm border border-gray-200 p-8 mb-8">
                    <h2 class="text-2xl font-bold text-gray-900 mb-2">Bienvenue dans pisciculture</h2>
                    <p class="text-gray-600">Gérez vos races de poissons, vos étangs et le suivi de croissance en toute simplicité.</p>
                </div>

                <!-- Stats Grid -->
                <div class="grid grid-cols-1 md:grid-cols-3 gap-6 mb-8">
                    <div class="bg-white p-6 rounded-lg shadow-sm border border-gray-200">
                        <div class="flex items-center">
                            <div class="p-3 rounded-full bg-blue-100 text-blue-600">
                                <svg class="h-6 w-6" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 11H5m14 0a2 2 0 012 2v6a2 2 0 01-2 2H5a2 2 0 01-2-2v-6a2 2 0 012-2m14 0V9a2 2 0 00-2-2M5 11V9a2 2 0 012-2m0 0V5a2 2 0 012-2h6a2 2 0 012 2v2M7 7h10" />
                                </svg>
                            </div>
                            <div class="ml-4">
                                <p class="text-sm font-medium text-gray-500">Races enregistrées</p>
                                <p class="text-2xl font-semibold text-gray-900">Gérer</p>
                            </div>
                        </div>
                        <a href="${pageContext.request.contextPath}/races" class="mt-4 block text-sm font-medium text-blue-600 hover:text-blue-500">Voir les races &rarr;</a>
                    </div>

                    <div class="bg-white p-6 rounded-lg shadow-sm border border-gray-200">
                        <div class="flex items-center">
                            <div class="p-3 rounded-full bg-green-100 text-green-600">
                                <svg class="h-6 w-6" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4 7v10c0 2.21 3.582 4 8 4s8-1.79 8-4V7M4 7c0 2.21 3.582 4 8 4s8-1.79 8-4M4 7c0-2.21 3.582-4 8-4s8 1.79 8 4m0 5c0 2.21-3.582 4-8 4s-8-1.79-8-4" />
                                </svg>
                            </div>
                            <div class="ml-4">
                                <p class="text-sm font-medium text-gray-500">Étangs</p>
                                <p class="text-2xl font-semibold text-gray-900">Suivi</p>
                            </div>
                        </div>
                        <a href="#" class="mt-4 block text-sm font-medium text-blue-600 hover:text-blue-500">Voir les étangs &rarr;</a>
                    </div>

                    <div class="bg-white p-6 rounded-lg shadow-sm border border-gray-200">
                        <div class="flex items-center">
                            <div class="p-3 rounded-full bg-purple-100 text-purple-600">
                                <svg class="h-6 w-6" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M13 7h8m0 0v8m0-8l-8 8-4-4-6 6" />
                                </svg>
                            </div>
                            <div class="ml-4">
                                <p class="text-sm font-medium text-gray-500">Statistiques</p>
                                <p class="text-2xl font-semibold text-gray-900">Analyses</p>
                            </div>
                        </div>
                        <a href="#" class="mt-4 block text-sm font-medium text-blue-600 hover:text-blue-500">Consulter &rarr;</a>
                    </div>
                </div>

                <!-- Recent Activity or Shortcuts -->
                <div class="bg-white rounded-lg shadow-sm border border-gray-200 overflow-hidden">
                    <div class="px-6 py-4 border-b border-gray-200 bg-gray-50">
                        <h3 class="text-sm font-semibold text-gray-900 uppercase tracking-wider">Actions Rapides</h3>
                    </div>
                    <div class="p-6 grid grid-cols-1 sm:grid-cols-2 gap-4">
                        <a href="${pageContext.request.contextPath}/races/new" class="flex items-center p-4 rounded-md border border-gray-200 hover:bg-gray-50 transition">
                            <div class="p-2 rounded bg-blue-50 text-blue-600 mr-4">
                                <svg class="h-5 w-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 4v16m8-8H4" />
                                </svg>
                            </div>
                            <span class="text-sm font-medium text-gray-700">Ajouter une nouvelle race</span>
                        </a>
                        <a href="#" class="flex items-center p-4 rounded-md border border-gray-200 hover:bg-gray-50 transition">
                            <div class="p-2 rounded bg-green-50 text-green-600 mr-4">
                                <svg class="h-5 w-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 4v16m8-8H4" />
                                </svg>
                            </div>
                            <span class="text-sm font-medium text-gray-700">Créer un nouvel étang</span>
                        </a>
                    </div>
                </div>
            </div>
        </main>
    </div>
</body>
</html>
