<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html lang="fr" class="h-full bg-white">
<head>
    <meta charset="UTF-8">
    <title>${title}</title>
    <script src="https://cdn.tailwindcss.com"></script>
</head>
<body class="h-full overflow-hidden flex">
    <jsp:include page="/WEB-INF/jsp/components/sidebar.jsp" />

    <div class="flex-1 flex flex-col min-w-0 overflow-hidden">
        <header class="bg-white border-b border-gray-200">
            <div class="px-4 sm:px-6 lg:px-8">
                <div class="flex justify-between h-16 items-center">
                    <h1 class="text-lg font-semibold text-gray-900">${title}</h1>
                </div>
            </div>
        </header>

        <main class="flex-1 overflow-y-auto bg-gray-50 p-6">
            <div class="max-w-3xl mx-auto">
                <div class="bg-white shadow sm:rounded-lg border border-gray-200 p-6">
                    <form action="${pageContext.request.contextPath}/poissons/save" method="POST" class="space-y-6">
                        <input type="hidden" name="id" value="${poisson.id}">
                        
                        <div>
                            <label for="nom" class="block text-sm font-medium text-gray-700">Nom du poisson</label>
                            <input type="text" name="nom" id="nom" value="${poisson.nom}" required class="mt-1 block w-full border border-gray-300 rounded-md shadow-sm py-2 px-3 focus:outline-none focus:ring-emerald-500 focus:border-emerald-500 sm:text-sm">
                        </div>

                        <div class="grid grid-cols-1 gap-y-6 gap-x-4 sm:grid-cols-2">
                            <div>
                                <label for="race" class="block text-sm font-medium text-gray-700">Race</label>
                                <select name="race.id" id="race" required class="mt-1 block w-full border border-gray-300 rounded-md shadow-sm py-2 px-3 focus:outline-none focus:ring-emerald-500 focus:border-emerald-500 sm:text-sm">
                                    <option value="">Sélectionner une race</option>
                                    <c:forEach items="${races}" var="r">
                                        <option value="${r.id}" ${poisson.race != null and poisson.race.id == r.id ? 'selected' : ''}>${r.nom}</option>
                                    </c:forEach>
                                </select>
                            </div>

                            <div>
                                <label for="poidsInitial" class="block text-sm font-medium text-gray-700">Poids initial (kg)</label>
                                <input type="number" step="0.01" name="poidsInitial" id="poidsInitial" value="${poisson.poidsInitial}" required class="mt-1 block w-full border border-gray-300 rounded-md shadow-sm py-2 px-3 focus:outline-none focus:ring-emerald-500 focus:border-emerald-500 sm:text-sm">
                            </div>

                            <div class="sm:col-span-1">
                                <label for="statut" class="block text-sm font-medium text-gray-700">Statut</label>
                                <select name="statutId" id="statut" required class="mt-1 block w-full border border-gray-300 rounded-md shadow-sm py-2 px-3 focus:outline-none focus:ring-emerald-500 focus:border-emerald-500 sm:text-sm">
                                    <option value="">Sélectionner un statut</option>
                                    <c:forEach items="${statuts}" var="s">
                                        <option value="${s.id}" ${poisson.currentStatut != null and poisson.currentStatut.id == s.id ? 'selected' : (poisson.id == null and s.nom == 'Vivant' ? 'selected' : '')}>${s.nom}</option>
                                    </c:forEach>
                                </select>
                            </div>

                            <div class="sm:col-span-1">
                                <label for="etang" class="block text-sm font-medium text-gray-700">Étang</label>
                                <select name="etangId" id="etang" required class="mt-1 block w-full border border-gray-300 rounded-md shadow-sm py-2 px-3 focus:outline-none focus:ring-emerald-500 focus:border-emerald-500 sm:text-sm">
                                    <option value="">Sélectionner un étang</option>
                                    <c:forEach items="${etangs}" var="e">
                                        <option value="${e.id}" ${poisson.currentEtang != null and poisson.currentEtang.id == e.id ? 'selected' : ''}>Étang #${e.id}</option>
                                    </c:forEach>
                                </select>
                            </div>
                        </div>

                        <div class="flex justify-end space-x-3">
                            <a href="${pageContext.request.contextPath}/poissons" class="bg-white py-2 px-4 border border-gray-300 rounded-md shadow-sm text-sm font-medium text-gray-700 hover:bg-gray-50 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-emerald-500">
                                Annuler
                            </a>
                            <button type="submit" class="inline-flex justify-center py-2 px-4 border border-transparent shadow-sm text-sm font-medium rounded-md text-white bg-emerald-600 hover:bg-emerald-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-emerald-500">
                                Enregistrer
                            </button>
                        </div>
                    </form>
                </div>
            </div>
        </main>
    </div>
</body>
</html>
