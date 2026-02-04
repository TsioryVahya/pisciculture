<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

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
                        <a href="${pageContext.request.contextPath}/poissons/new" class="inline-flex items-center px-4 py-2 border border-transparent text-sm font-medium rounded-md shadow-sm text-white bg-emerald-600 hover:bg-emerald-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-emerald-500">
                            Nouveau Poisson
                        </a>
                    </div>
                </div>
            </div>
        </header>

        <main class="flex-1 overflow-y-auto bg-gray-50 p-6">
            <div class="max-w-7xl mx-auto">
                <div class="bg-white shadow overflow-hidden sm:rounded-lg border border-gray-200">
                    <table class="min-w-full divide-y divide-gray-200">
                        <thead class="bg-gray-50">
                            <tr>
                                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Nom</th>
                                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Race</th>
                                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Étang</th>
                                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Poids Initial</th>
                                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Poids Actuel</th>
                                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Statut</th>
                                <th class="px-6 py-3 text-right text-xs font-medium text-gray-500 uppercase tracking-wider">Actions</th>
                            </tr>
                        </thead>
                        <tbody class="bg-white divide-y divide-gray-200">
                            <c:forEach items="${poissons}" var="p">
                                <tr>
                                    <td class="px-6 py-4 whitespace-nowrap text-sm font-medium text-gray-900">
                                        <a href="${pageContext.request.contextPath}/poissons/history/${p.id}" class="text-emerald-600 hover:text-emerald-900">
                                            ${p.nom}
                                        </a>
                                    </td>
                                    <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-500">${p.race.nom}</td>
                                    <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                                        <c:choose>
                                            <c:when test="${not empty p.currentEtang}">
                                                <span class="inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium bg-indigo-100 text-indigo-800">
                                                    Étang #${p.currentEtang.id}
                                                </span>
                                            </c:when>
                                            <c:otherwise>
                                                <span class="text-gray-400 italic">Non assigné</span>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                                        <fmt:formatNumber value="${p.poidsInitial}" pattern="#,##0.000000" /> kg
                                    </td>
                                    <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-900 font-bold">
                                        <c:choose>
                                            <c:when test="${not empty p.currentPoids}">
                                                <fmt:formatNumber value="${p.currentPoids}" pattern="#,##0.000000" /> kg
                                            </c:when>
                                            <c:otherwise>
                                                <fmt:formatNumber value="${p.poidsInitial}" pattern="#,##0.000000" /> kg
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                                        <c:choose>
                                            <c:when test="${p.currentStatut != null and p.currentStatut.nom == 'Vivant'}">
                                                <span class="px-2 inline-flex text-xs leading-5 font-semibold rounded-full bg-green-100 text-green-800">
                                                    ${p.currentStatut.nom}
                                                </span>
                                            </c:when>
                                            <c:when test="${p.currentStatut != null and p.currentStatut.nom == 'Vendu'}">
                                                <span class="px-2 inline-flex text-xs leading-5 font-semibold rounded-full bg-blue-100 text-blue-800">
                                                    ${p.currentStatut.nom}
                                                </span>
                                            </c:when>
                                            <c:when test="${p.currentStatut != null and p.currentStatut.nom == 'Mort'}">
                                                <span class="px-2 inline-flex text-xs leading-5 font-semibold rounded-full bg-red-100 text-red-800">
                                                    ${p.currentStatut.nom}
                                                </span>
                                            </c:when>
                                            <c:otherwise>
                                                <span class="px-2 inline-flex text-xs leading-5 font-semibold rounded-full bg-gray-100 text-gray-800">
                                                    ${p.currentStatut != null ? p.currentStatut.nom : 'Non défini'}
                                                </span>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td class="px-6 py-4 whitespace-nowrap text-right text-sm font-medium">
                                        <a href="${pageContext.request.contextPath}/poissons/history/${p.id}" class="text-indigo-600 hover:text-indigo-900 mr-3">Historique</a>
                                        <a href="${pageContext.request.contextPath}/poissons/edit/${p.id}" class="text-emerald-600 hover:text-emerald-900 mr-3">Modifier</a>
                                        <a href="${pageContext.request.contextPath}/poissons/delete/${p.id}" class="text-red-600 hover:text-red-900" onclick="return confirm('Êtes-vous sûr ?')">Supprimer</a>
                                    </td>
                                </tr>
                            </c:forEach>
                            <c:if test="${empty poissons}">
                                <tr>
                                    <td colspan="6" class="px-6 py-10 text-center text-sm text-gray-500">
                                        Aucun poisson enregistré.
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
