<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="layout" tagdir="/WEB-INF/tags" %>

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
                    <div class="flex items-center space-x-3">
                        <a href="${pageContext.request.contextPath}/races/new" class="inline-flex items-center px-4 py-2 border border-transparent text-sm font-medium rounded-md shadow-sm text-white bg-emerald-600 hover:bg-emerald-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-emerald-500">
                            Nouvelle Race
                        </a>
                    </div>
                </div>
            </div>
        </header>

        <main class="flex-1 overflow-y-auto bg-gray-50 p-6">
            <div class="max-w-7xl mx-auto">
                <div class="bg-white shadow sm:rounded-lg border border-gray-200 overflow-x-auto">
                    <table class="min-w-full divide-y divide-gray-200">
                        <thead class="bg-gray-50">
                            <tr>
                                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Nom</th>
                                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Prix Achat (kg)</th>
                                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Prix Vente (kg)</th>
                                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Poids Max</th>
                                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Croissance/j (g)</th>
                                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Besoin Prot. (g)</th>
                                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Besoin Gluc. (g)</th>
                                <th class="px-6 py-3 text-right text-xs font-medium text-gray-500 uppercase tracking-wider">Actions</th>
                            </tr>
                        </thead>
                        <tbody class="bg-white divide-y divide-gray-200">
                            <c:forEach items="${races}" var="race">
                                <tr>
                                    <td class="px-6 py-4 whitespace-nowrap text-sm font-medium text-gray-900">${race.nom}</td>
                                    <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-500">${race.prixAchatParKg} Ar</td>
                                    <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-500">${race.prixVenteParKg} Ar</td>
                                    <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-500">${race.poidsMax} kg</td>
                                    <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-500">${race.capaciteAugmentationPoids} g</td>
                                    <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-500">${race.besoinProteine} g</td>
                                    <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-500">${race.besoinGlucide} g</td>
                                    <td class="px-6 py-4 whitespace-nowrap text-right text-sm font-medium">
                                        <a href="${pageContext.request.contextPath}/races/history/${race.id}" class="text-green-600 hover:text-green-900 mr-3">Historique</a>
                                        <a href="${pageContext.request.contextPath}/races/edit/${race.id}" class="text-emerald-600 hover:text-emerald-900 mr-3">Modifier</a>
                                        <a href="${pageContext.request.contextPath}/races/delete/${race.id}" class="text-red-600 hover:text-red-900" onclick="return confirm('Êtes-vous sûr ?')">Supprimer</a>
                                    </td>
                                </tr>
                            </c:forEach>
                            <c:if test="${empty races}">
                                <tr>
                                    <td colspan="8" class="px-6 py-10 text-center text-sm text-gray-500">
                                        Aucune race enregistrée.
                                    </td>
                                </tr>
                            </c:if>
                        </tbody>
                    </table>
                </div>
            </div>
        </main>
    </div>
</body>
</html>
