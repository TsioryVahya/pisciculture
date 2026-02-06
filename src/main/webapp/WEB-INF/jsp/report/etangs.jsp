<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<!DOCTYPE html>
<html lang="fr" class="h-full bg-white">
<head>
    <meta charset="UTF-8">
    <title>${title}</title>
    <script src="https://cdn.tailwindcss.com"></script>
</head>
<body class="h-full overflow-hidden flex">
    <!-- Sidebar Component -->
    <jsp:include page="/WEB-INF/jsp/components/sidebar.jsp" />

    <!-- Main Content -->
    <div class="flex-1 flex flex-col min-w-0 overflow-hidden">
        <!-- Top Navigation -->
        <header class="bg-white border-b border-gray-200">
            <div class="px-4 sm:px-6 lg:px-8">
                <div class="flex justify-between h-16 items-center">
                    <h1 class="text-lg font-semibold text-gray-900">${title}</h1>
                </div>
            </div>
        </header>

        <!-- Page Content -->
        <main class="flex-1 overflow-y-auto bg-gray-50 p-6">
            <div class="max-w-7xl mx-auto">
                
                <!-- Reporting par Étang -->
                <div class="mb-10">
                    <div class="mb-6">
                        <h2 class="text-2xl font-bold text-gray-900">Reporting par Étang</h2>
                        <p class="mt-1 text-sm text-gray-600">
                            Synthèse des revenus, dépenses et bénéfices pour chaque étang.
                        </p>
                    </div>

                    <div class="grid grid-cols-1 gap-5 sm:grid-cols-2 lg:grid-cols-3 mb-6">
                        <c:forEach items="${etangRows}" var="row">
                            <div class="bg-white overflow-hidden shadow rounded-lg border border-gray-200">
                                <div class="px-4 py-5 sm:p-6">
                                    <div class="flex items-center justify-between mb-4">
                                        <h3 class="text-lg font-medium text-gray-900">Étang #${row.etang.id}</h3>
                                        <span class="inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium bg-emerald-100 text-emerald-800">
                                            ${row.etang.surface} m²
                                        </span>
                                    </div>
                                    <dl class="grid grid-cols-1 gap-4">
                                        <div class="flex justify-between">
                                            <dt class="text-sm font-medium text-gray-500">Revenu</dt>
                                            <dd class="text-sm font-semibold text-emerald-600"><fmt:formatNumber value="${row.revenu}" pattern="#,##0" /> Ar</dd>
                                        </div>
                                        <div class="flex justify-between">
                                            <dt class="text-sm font-medium text-gray-500">Dépense (Alim.)</dt>
                                            <dd class="text-sm font-semibold text-red-600"><fmt:formatNumber value="${row.depense}" pattern="#,##0" /> Ar</dd>
                                        </div>
                                        <div class="border-t border-gray-100 pt-3 flex justify-between">
                                            <dt class="text-sm font-bold text-gray-900">Bénéfice</dt>
                                            <dd class="text-sm font-bold ${row.benefice >= 0 ? 'text-emerald-700' : 'text-red-700'}">
                                                <fmt:formatNumber value="${row.benefice}" pattern="#,##0" /> Ar
                                            </dd>
                                        </div>
                                    </dl>
                                </div>
                            </div>
                        </c:forEach>
                    </div>
                </div>

                <!-- Reporting détaillé des Poissons (équivalent à /report/poissons) -->
                <div class="mb-6">
                    <h2 class="text-2xl font-bold text-gray-900">Détails par Poisson</h2>
                    <p class="mt-1 text-sm text-gray-600">
                        Visualisation détaillée pour chaque poisson présent dans les étangs.
                    </p>
                </div>

                <div class="bg-white shadow overflow-hidden sm:rounded-lg border border-gray-200">
                    <table class="min-w-full divide-y divide-gray-200">
                        <thead class="bg-gray-50">
                            <tr>
                                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Poisson</th>
                                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Race</th>
                                <th class="px-6 py-3 text-right text-xs font-medium text-gray-500 uppercase tracking-wider">Poids Initial</th>
                                <th class="px-6 py-3 text-right text-xs font-medium text-gray-500 uppercase tracking-wider">Poids Atteint</th>
                                <th class="px-6 py-3 text-right text-xs font-medium text-gray-500 uppercase tracking-wider">Dépense (Ar)</th>
                                <th class="px-6 py-3 text-right text-xs font-medium text-gray-500 uppercase tracking-wider">Revenu (Ar)</th>
                                <th class="px-6 py-3 text-right text-xs font-medium text-gray-500 uppercase tracking-wider">Bénéfice (Ar)</th>
                            </tr>
                        </thead>
                        <tbody class="bg-white divide-y divide-gray-200">
                            <c:forEach items="${poissonRows}" var="pRow">
                                <tr>
                                    <td class="px-6 py-4 whitespace-nowrap text-sm font-medium">
                                        <a href="${pageContext.request.contextPath}/poissons/history/${pRow.poisson.id}" class="text-emerald-600 hover:text-emerald-900">
                                            ${pRow.poisson.nom}
                                        </a>
                                    </td>
                                    <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                                        ${pRow.race.nom}
                                    </td>
                                    <td class="px-6 py-4 whitespace-nowrap text-sm text-right text-gray-700">
                                        <fmt:formatNumber value="${pRow.poidsInitial}" pattern="#,##0.00" /> kg
                                    </td>
                                    <td class="px-6 py-4 whitespace-nowrap text-sm text-right text-gray-700">
                                        <fmt:formatNumber value="${pRow.poidsAtteint}" pattern="#,##0.00" /> kg
                                    </td>
                                    <td class="px-6 py-4 whitespace-nowrap text-sm text-right text-red-600">
                                        <fmt:formatNumber value="${pRow.coutAlimentation}" pattern="#,##0" />
                                    </td>
                                    <td class="px-6 py-4 whitespace-nowrap text-sm text-right text-emerald-600 font-medium">
                                        <fmt:formatNumber value="${pRow.revenu}" pattern="#,##0" />
                                    </td>
                                    <td class="px-6 py-4 whitespace-nowrap text-sm text-right">
                                        <span class="font-bold ${pRow.benefice >= 0 ? 'text-emerald-700' : 'text-red-700'}">
                                            <fmt:formatNumber value="${pRow.benefice}" pattern="#,##0" />
                                        </span>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>

            </div>
        </main>
    </div>
</body>
</html>
